package com.example.urbanmarket.exception.userExceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User was not found");
    }
}
