package com.example.urbanmarket.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {

    private static final String NOT_FOUND_EXCEPTION_TEXT = "%s not found";
    private static final String WITH_ID_NOT_FOUND_EXCEPTION_TEXT = "%s with id/code: %s not found.";
    private static final String NOT_FOUND_CUSTOM_EXCEPTION_TEXT = "%s with %s: %s not found";

    public CustomNotFoundException(String objectName) {
        super(NOT_FOUND_EXCEPTION_TEXT.formatted(objectName));
    }

    public CustomNotFoundException(String objectName, String id) {
        super(WITH_ID_NOT_FOUND_EXCEPTION_TEXT.formatted(objectName, id));
    }

    public CustomNotFoundException(String objectName, String propertyName, String value) {
        super(NOT_FOUND_CUSTOM_EXCEPTION_TEXT.formatted(objectName, propertyName, value));
    }
}