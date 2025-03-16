package com.example.urbanmarket.exception.exceptions.user;

public class UnconfirmedPasswordChangeException extends RuntimeException{
    private static final String UNCONFIRMED_PASSWORD_CHANGE_EXCEPTION_TEXT = "You need to confirm change of your password to log in. Please check your email: %s";

    public UnconfirmedPasswordChangeException(String email) {
        super(String.format(UNCONFIRMED_PASSWORD_CHANGE_EXCEPTION_TEXT, email));
    }
}
