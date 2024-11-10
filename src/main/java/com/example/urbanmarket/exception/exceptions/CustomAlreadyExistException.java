package com.example.urbanmarket.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomAlreadyExistException extends RuntimeException {

    private static final String ALREADY_EXIST_EXCEPTION_NAME = "This {} already exist. Name = {}";
    private static final String ALREADY_EXIST_EXCEPTION_PROPERTY_NAME_AND_VALUE = "This {} already exist. {} = {}";


    public CustomAlreadyExistException(String objectName, String name) {
        super(ALREADY_EXIST_EXCEPTION_NAME.formatted(objectName, name));
    }

    public CustomAlreadyExistException(String objectName, String propertyName, String value) {
        super(ALREADY_EXIST_EXCEPTION_PROPERTY_NAME_AND_VALUE.formatted(objectName, propertyName, value));
    }


}