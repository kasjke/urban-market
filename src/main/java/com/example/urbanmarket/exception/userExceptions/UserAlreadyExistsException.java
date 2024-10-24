package com.example.urbanmarket.exception.userExceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email){
        super("User with email: %s already exist".formatted(email));
    }
}
