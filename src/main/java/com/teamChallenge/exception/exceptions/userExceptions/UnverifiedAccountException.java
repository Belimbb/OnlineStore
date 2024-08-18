package com.teamChallenge.exception.exceptions.userExceptions;

public class UnverifiedAccountException extends RuntimeException {

    private static final String UNVERIFIED_ACCOUNT_EXCEPTION_TEXT = "You need to verify your account to log in. Please check your email: %s";

    public UnverifiedAccountException(String email) {
        super(String.format(UNVERIFIED_ACCOUNT_EXCEPTION_TEXT, email));
    }
}
