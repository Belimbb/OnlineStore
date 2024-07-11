package com.teamChallenge.entity.Figures;

import com.teamChallenge.exception.exceptions.figureExceptions.FigureAlreadyExistException;
import com.teamChallenge.exception.exceptions.figureExceptions.FigureNotFoundException;

import java.util.List;
import java.util.UUID;

public interface FigureService {
    FigureDto createFigure (String name, String shortDescription, String longDescription, Enum<?> subCategory, int price, int amount, String color, List<String> images) throws FigureAlreadyExistException;
    FigureDto getById (UUID id) throws FigureNotFoundException;
    List<FigureDto> getAllFigures ();
    FigureDto updateFigure (FigureDto figureDto);
    boolean deleteFigure (UUID id);
}
