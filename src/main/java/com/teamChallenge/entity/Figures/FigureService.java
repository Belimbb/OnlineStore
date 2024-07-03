package com.teamChallenge.entity.Figures;

import com.teamChallenge.entity.Products.Category;
import com.teamChallenge.exception.exceptions.productExceptions.ProductAlreadyExistException;
import com.teamChallenge.exception.exceptions.productExceptions.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

public interface FigureService {
    FigureDto createFigure (String name, String shortDescription, String longDescription, Category category, int price, int amount, String color, List<String> images) throws ProductAlreadyExistException;
    FigureDto getById (UUID id) throws ProductNotFoundException;
    List<FigureDto> getAllFigures ();
    FigureDto updateFigure (FigureDto figureDto);
    boolean deleteFigure (UUID id);
}
