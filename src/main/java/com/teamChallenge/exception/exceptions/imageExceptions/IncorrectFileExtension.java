package com.teamChallenge.exception.exceptions.imageExceptions;

public class IncorrectFileExtension extends RuntimeException {

    public static final String INCORRECT_FILE_EXTENSION_TEXT = "Incorrect file extension (%s). Please, change the upload file.";

    public IncorrectFileExtension(String extension) {
        super(String.format(INCORRECT_FILE_EXTENSION_TEXT, extension));
    }
}
