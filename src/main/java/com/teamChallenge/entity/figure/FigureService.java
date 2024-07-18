package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.SubCategory;
import com.teamChallenge.exception.exceptions.figureExceptions.FigureAlreadyExistException;
import com.teamChallenge.exception.exceptions.figureExceptions.FigureNotFoundException;

import java.util.List;

public interface FigureService {
    FigureDto createFigure (String name, String shortDescription, String longDescription, SubCategory subCategory, int price, int amount, String color, List<String> images) throws FigureAlreadyExistException;
    FigureDto getById (String id) throws FigureNotFoundException;
    List<FigureDto> getAllFigures ();
    FigureDto updateFigure (FigureDto figureDto);
    void deleteFigure (String id);
}
