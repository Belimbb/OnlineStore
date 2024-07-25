package com.teamChallenge.exception.exceptions.generalExceptions;

public class SomethingWentWrongException extends RuntimeException {

    public SomethingWentWrongException() {
        super("Something went wrong, please contact the administrator");
    }
}
