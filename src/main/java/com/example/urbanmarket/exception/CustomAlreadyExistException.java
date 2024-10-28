package com.example.urbanmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomAlreadyExistException extends RuntimeException {

    private static final String ALREADY_EXIST_EXCEPTION_NAME = "This %s already exist. Name = %s";
    private static final String ALREADY_EXIST_EXCEPTION_PROPERTY_NAME_AND_VALUE = "This %s already exist. %s = %s";


    public CustomAlreadyExistException(String objectName, String name) {
        super(ALREADY_EXIST_EXCEPTION_NAME.formatted(objectName, name));
    }

    public CustomAlreadyExistException(String objectName, String propertyName, String value) {
        super(ALREADY_EXIST_EXCEPTION_PROPERTY_NAME_AND_VALUE.formatted(objectName, propertyName, value));
    }


}