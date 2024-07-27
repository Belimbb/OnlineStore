package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface FigureService {
    FigureResponseDto createFigure (String name, String shortDescription, String longDescription, SubCategoryEntity subCategory, Labels label, int currentPrice, int oldPrice, int amount, String color, List<String> images) throws CustomAlreadyExistException;
    FigureResponseDto getById (String id) throws CustomNotFoundException;
    List<FigureResponseDto> getAllFigures ();
    FigureResponseDto updateFigure (String id, FigureRequestDto dto) throws CustomNotFoundException;
    void deleteFigure (String id) throws CustomNotFoundException;
}
