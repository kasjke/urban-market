package com.example.urbanmarket.exception;

import com.example.urbanmarket.exception.exceptions.general.CustomAlreadyExistException;
import com.example.urbanmarket.exception.exceptions.general.CustomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                ));

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, List<String>>> handleGeneralException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        return getErrorsMap(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> notFoundException(CustomNotFoundException e) {
        return getErrorsMap(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomAlreadyExistException.class)
    public ResponseEntity<Map<String, List<String>>> alreadyExistException(CustomAlreadyExistException e) {
        return getErrorsMap(e, HttpStatus.CONFLICT);
    }


    private Map<String, List<String>> getErrorsMap(Map<String, List<String>> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", new ArrayList<>());
        errors.forEach((key, value) -> {
            value.forEach(v -> errorResponse.get("errors").add(String.format("%s: %s", key, v)));
        });
        return errorResponse;
    }

    private ResponseEntity<Map<String, List<String>>> getErrorsMap(Throwable ex, HttpStatus status) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(map, new HttpHeaders(), status);
    }
    }