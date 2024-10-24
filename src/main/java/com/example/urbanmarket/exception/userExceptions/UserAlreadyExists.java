package com.example.urbanmarket.exception.userExceptions;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String email){
        super(String.format("User with email: %s already exist", email));
    }
}
