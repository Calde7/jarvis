package com.tenpo.jarvis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class APIException {
    @Getter
    @Setter
    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    @Getter
    @Setter
    private LocalDateTime timestamp;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String errorMessage;

    @Getter
    @Setter
    private String exceptionName;
}
