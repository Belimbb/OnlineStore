package com.teamChallenge.exception.exceptions.figureExceptions;

import java.util.UUID;

public class FigureAlreadyExistException extends Exception{
    private static final String FIGURE_ALREADY_EXIST_EXCEPTION_ID = "Figure with id = %s already exist.";
    private static final String FIGURE_ALREADY_EXIST_EXCEPTION_NAME = "Figure with name = %s already exist.";

    public FigureAlreadyExistException(UUID id) {
        super(String.format(FIGURE_ALREADY_EXIST_EXCEPTION_ID, id));
    }

    public FigureAlreadyExistException(String name) {
        super(String.format(FIGURE_ALREADY_EXIST_EXCEPTION_NAME, name));
    }
}
