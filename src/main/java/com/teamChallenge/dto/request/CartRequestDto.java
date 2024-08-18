package com.teamChallenge.dto.request;

import com.teamChallenge.dto.request.figure.FigureInCartRequestDto;

import java.util.List;

public record CartRequestDto(String promoCode, List<FigureInCartRequestDto> figures) {
}