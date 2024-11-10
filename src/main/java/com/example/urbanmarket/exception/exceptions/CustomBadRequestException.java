package com.example.urbanmarket.exception.exceptions;

public class CustomBadRequestException extends RuntimeException {
    public static final String BAD_REQUEST_EXCEPTION = "This request is not valid";


    public CustomBadRequestException() {
        super(BAD_REQUEST_EXCEPTION);
    }

    public CustomBadRequestException(String message) {
        super(message);
    }
}
