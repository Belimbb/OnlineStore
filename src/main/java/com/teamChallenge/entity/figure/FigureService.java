package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface FigureService {
    FigureResponseDto create(FigureRequestDto figureRequestDto) throws CustomAlreadyExistException;
    FigureResponseDto getById (String id) throws CustomNotFoundException;
    List<FigureResponseDto> getAll(String filter, String label, String startPrice, String endPrice, String page, String size);
    FigureResponseDto update(String id, FigureRequestDto dto) throws CustomNotFoundException;
    void delete(String id) throws CustomNotFoundException;
}
