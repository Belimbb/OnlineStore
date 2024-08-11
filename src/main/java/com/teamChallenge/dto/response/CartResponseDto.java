package com.teamChallenge.dto.response;

import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;

import java.util.List;

public record CartResponseDto(String id, List<FigureInCartOrderResponseDto> figures, int totalPrice) {
}
