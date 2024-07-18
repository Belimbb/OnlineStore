package com.teamChallenge.exception.exceptions.figureExceptions;

import java.util.UUID;

public class FigureNotFoundException extends RuntimeException{
    private static final String FIGURE_NOT_FOUND_EXCEPTION_TEXT = "Figure not found";
    private static final String FIGURE_WITH_NAME_NOT_FOUND_EXCEPTION_TEXT = "Figure with name/id = %s not found.";


    public FigureNotFoundException() {
        super(FIGURE_NOT_FOUND_EXCEPTION_TEXT);
    }

    public FigureNotFoundException(String name) {
        super(String.format(FIGURE_WITH_NAME_NOT_FOUND_EXCEPTION_TEXT, name));
    }
}