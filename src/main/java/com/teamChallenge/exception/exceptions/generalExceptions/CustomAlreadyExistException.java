package com.teamChallenge.exception.exceptions.generalExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomAlreadyExistException extends RuntimeException {

    private static final String ALREADY_EXIST_EXCEPTION_ID = "This %s already exist. Name = %s";

    public CustomAlreadyExistException(String objectName, String name) {
        super(String.format(ALREADY_EXIST_EXCEPTION_ID, objectName, name));
    }
}
