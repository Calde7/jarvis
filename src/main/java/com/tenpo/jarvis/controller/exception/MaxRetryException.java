package com.tenpo.jarvis.controller.exception;

public class MaxRetryException extends RuntimeException {
    public MaxRetryException(final String message){
        super(message);
    }
}
