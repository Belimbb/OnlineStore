package com.teamChallenge.exception.exceptions.generalExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {

    private static final String NOT_FOUND_EXCEPTION_TEXT = "%s not found";
    private static final String WITH_ID_NOT_FOUND_EXCEPTION_TEXT = "%s with id: %s not found.";

    public CustomNotFoundException(String objectName) {
        super(String.format(NOT_FOUND_EXCEPTION_TEXT, objectName));
    }

    public CustomNotFoundException(String objectName, String id) {
        super(String.format(WITH_ID_NOT_FOUND_EXCEPTION_TEXT, objectName, id));
    }
}
