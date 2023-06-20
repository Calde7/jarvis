package com.tenpo.jarvis.controller.handler;

import com.tenpo.jarvis.controller.exception.RequestExecutionException;
import com.tenpo.jarvis.controller.exception.TooManyRequestException;
import com.tenpo.jarvis.dto.APIException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.tenpo.jarvis.config.ErrorMessage.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class APIExceptionHandler  extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildResponseEntity(APIException.builder().status(HttpStatus.METHOD_NOT_ALLOWED)
                .timestamp(LocalDateTime.now())
                .message(ERROR_INVALID_HTTP_METHOD)
                .errorMessage(ex.getMessage())
                .exceptionName(HttpRequestMethodNotSupportedException.class.getName())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return buildResponseEntity(APIException.builder().status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message(ERROR_INVALID_PATH_PARAM)
                .errorMessage(ex.getMessage())
                .exceptionName(TypeMismatchException.class.getName())
                .build());
    }

    @ExceptionHandler(TooManyRequestException.class)
    protected ResponseEntity<Object> handleTooManyRequests(TooManyRequestException ex) {
        final String error = ex.getMessage();
        return buildResponseEntity(APIException.builder().status(HttpStatus.TOO_MANY_REQUESTS)
                .timestamp(LocalDateTime.now())
                .message(error)
                .errorMessage(ex.getMessage())
                .exceptionName(TooManyRequestException.class.getName())
                .build());
    }

    @ExceptionHandler(RequestExecutionException.class)
    protected ResponseEntity<Object> handleAPIExecutionError(RequestExecutionException ex) {
        return buildResponseEntity(APIException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .message(ERROR_REQUEST_SERVICE)
                .errorMessage(ex.getMessage())
                .exceptionName(RequestExecutionException.class.getName())
                .build());
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> handleSQLException(SQLException ex) {
        final String error = ex.getMessage();
        return buildResponseEntity(APIException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .message(error)
                .errorMessage(ex.getMessage())
                .exceptionName(SQLException.class.getName())
                .build());
    }

    private ResponseEntity<Object> buildResponseEntity(APIException exception) {
        return new ResponseEntity(exception, exception.getStatus());
    }
}
