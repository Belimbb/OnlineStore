package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface FigureService {
    FigureResponseDto createFigure (FigureRequestDto figureRequestDto) throws CustomAlreadyExistException;
    FigureResponseDto getById (String id) throws CustomNotFoundException;
    List<FigureResponseDto> getAllFigures (String filter, String label, String startPrice, String endPrice, String page, String size);
    FigureResponseDto updateFigure (String id, FigureRequestDto dto) throws CustomNotFoundException;
    void deleteFigure (String id) throws CustomNotFoundException;
}
