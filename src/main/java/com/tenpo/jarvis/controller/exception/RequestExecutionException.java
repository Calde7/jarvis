package com.tenpo.jarvis.controller.exception;

public class RequestExecutionException extends RuntimeException {
    public RequestExecutionException(final String message){
        super(message);
    }
}

