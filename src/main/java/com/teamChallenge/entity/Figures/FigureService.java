package com.teamChallenge.entity.Figures;

import com.teamChallenge.exception.exceptions.productExceptions.ProductAlreadyExistException;
import com.teamChallenge.exception.exceptions.productExceptions.ProductNotFoundException;

import java.util.List;

public interface FigureService {
    FigureDto createFigure (String name, String shortDescription, String longDescription, Enum category, int price, int amount, String color, List<String> images) throws ProductAlreadyExistException;
    FigureDto getById (String id) throws ProductNotFoundException;
    List<FigureDto> getAllFigures ();
    FigureDto updateFigure (FigureDto figureDto);
    boolean deleteFigure (String id);
}
