package com.teamChallenge.exception.exceptions.imageExceptions;

public class UnableToUploadImageException extends RuntimeException {

    public static final String UNABLE_TO_UPLOAD_IMAGE_EXCEPTION_TEXT = "Unable to upload this image (filename: %s) to our storage. " +
            "Please, contact the administrator.";

    public UnableToUploadImageException(String filename) {
        super(String.format(UNABLE_TO_UPLOAD_IMAGE_EXCEPTION_TEXT, filename));
    }
}
