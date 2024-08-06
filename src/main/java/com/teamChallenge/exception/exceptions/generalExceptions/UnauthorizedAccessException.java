package com.teamChallenge.exception.exceptions.generalExceptions;


public class UnauthorizedAccessException extends Exception{
    private static final String NO_PERMISSION = "You don't have permission to this data";


    public UnauthorizedAccessException() {
        super(NO_PERMISSION);
    }
}
