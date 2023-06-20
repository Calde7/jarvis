package com.tenpo.jarvis.controller.exception;

public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException(final String message){
        super(message);
    }
}