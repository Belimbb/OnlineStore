package com.teamChallenge.exception.exceptions.imageExceptions;

public class IncorrectFileExtensionException extends RuntimeException {

    public static final String INCORRECT_FILE_EXTENSION_EXCEPTION_TEXT = "Incorrect file extension (%s). Please, change the upload file.";

    public IncorrectFileExtensionException(String extension) {
        super(String.format(INCORRECT_FILE_EXTENSION_EXCEPTION_TEXT, extension));
    }
}
