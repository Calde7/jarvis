package com.tenpo.jarvis.controller;

import com.tenpo.jarvis.controller.exception.CacheException;
import com.tenpo.jarvis.controller.exception.RequestExecutionException;
import com.tenpo.jarvis.controller.exception.TooManyRequestException;
import com.tenpo.jarvis.dto.OperationResponseDTO;
import com.tenpo.jarvis.dto.TrackingDTO;
import com.tenpo.jarvis.entity.Tracking;
import com.tenpo.jarvis.service.operation.OperationService;
import com.tenpo.jarvis.service.tracking.TrackingService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.tenpo.jarvis.config.ErrorMessage.ERROR_RATE_LIMIT_EXCEEDED;
import static com.tenpo.jarvis.config.Router.*;

@RestController
@RequestMapping(API_V1)
public class OperationController {

    @Autowired
    private OperationService operationService;

    @Autowired
    private TrackingService trackingService;

    public final Bucket bucket;
    public final Integer requestByMinute = 3;

    public OperationController() {
        final Bandwidth limit = Bandwidth.classic(requestByMinute, Refill.greedy(requestByMinute, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    private boolean validateMaxRequestByMinute() {
        if (!bucket.tryConsume(1)) {
            return true;
        }
        return false;
    }

    @GetMapping(PING)
    public String ping(final HttpServletRequest request) {
        if (validateMaxRequestByMinute()) {
            trackingService.save(Tracking.builder().url(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .error(String.format(ERROR_RATE_LIMIT_EXCEEDED, requestByMinute))
                    .createdAt(LocalDateTime.now())
                    .build());
            throw new TooManyRequestException(String.format(ERROR_RATE_LIMIT_EXCEEDED, requestByMinute));
        }
        trackingService.save(Tracking.builder().url(request.getRequestURI())
                .httpMethod(request.getMethod())
                .statusCode(String.valueOf(HttpStatus.OK.value()))
                .createdAt(LocalDateTime.now())
                .build());
        return "pong";
    }

    @GetMapping(OPERATION_CALCULATE)
    private ResponseEntity<OperationResponseDTO> calculate(
            final @PathVariable("number1") BigDecimal number1,
            final @PathVariable("number2") BigDecimal number2,
            final HttpServletRequest request) {

        if (validateMaxRequestByMinute()) {
            trackingService.save(Tracking.builder().url(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .error(String.format(ERROR_RATE_LIMIT_EXCEEDED, requestByMinute))
                    .createdAt(LocalDateTime.now())
                    .build());
            throw new TooManyRequestException(String.format(ERROR_RATE_LIMIT_EXCEEDED, requestByMinute));
        }

        try {
            ResponseEntity<OperationResponseDTO> response = ResponseEntity.ok(operationService.calculate(number1, number2));

            trackingService.save(Tracking.builder()
                    .url(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .statusCode(String.valueOf(response.getStatusCode().value()))
                    .response(response.getBody().toString().getBytes())
                    .createdAt(LocalDateTime.now()).build());

            return response;

        } catch (CacheException e){
            trackingService.save(Tracking.builder()
                    .url(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .statusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .createdAt(LocalDateTime.now()).build());
            throw new RequestExecutionException(e.getMessage());
        }
    }

    @GetMapping(TRACKING_FIND_ALL)
    private ResponseEntity<List<TrackingDTO>> getHistoryTracking(
            final HttpServletRequest request,
            final @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            final @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        if (validateMaxRequestByMinute()) {
            trackingService.save(Tracking.builder().url(request.getRequestURI())
                    .httpMethod(request.getMethod())
                    .statusCode(String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()))
                    .error(String.format(ERROR_RATE_LIMIT_EXCEEDED, requestByMinute))
                    .createdAt(LocalDateTime.now())
                    .build());
            throw new TooManyRequestException(String.format(ERROR_RATE_LIMIT_EXCEEDED, requestByMinute));
        }

        final ResponseEntity<List<TrackingDTO>> response = ResponseEntity.ok(trackingService.findAll(offset, pageSize)
                .stream().map(TrackingDTO::parseEntity)
                .collect(Collectors.toList()));

        trackingService.save(Tracking.builder()
                .url(request.getRequestURI())
                .httpMethod(request.getMethod())
                .statusCode(String.valueOf(response.getStatusCode().value()))
                .response(response.getBody().toString().getBytes())
                .createdAt(LocalDateTime.now()).build());

        return response;
    }
}
