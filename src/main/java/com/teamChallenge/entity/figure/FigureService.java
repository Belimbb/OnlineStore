package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.SubCategory;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface FigureService {
    FigureDto createFigure (String name, String shortDescription, String longDescription, SubCategory subCategory, int price, int amount, String color, List<String> images) throws CustomAlreadyExistException;
    FigureDto getById (String id) throws CustomNotFoundException;
    List<FigureDto> getAllFigures ();
    FigureDto updateFigure (FigureDto figureDto);
    void deleteFigure (String id);
}
