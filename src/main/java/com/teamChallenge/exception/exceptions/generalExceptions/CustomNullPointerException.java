package com.teamChallenge.exception.exceptions.generalExceptions;

public class CustomNullPointerException extends RuntimeException {

    public static final String NULL_POINTER_EXCEPTION_TEXT = "Value '%s' cannot be assigned as number (int value)";

    public CustomNullPointerException(String value) {
        super(String.format(NULL_POINTER_EXCEPTION_TEXT, value));
    }
}
