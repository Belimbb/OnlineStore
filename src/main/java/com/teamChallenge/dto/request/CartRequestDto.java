package com.teamChallenge.dto.request;

import com.teamChallenge.dto.request.figure.FigureInCartOrderRequestDto;

import java.util.List;

public record CartRequestDto(List<FigureInCartOrderRequestDto> figures) {
}
