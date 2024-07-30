package com.teamChallenge.dto.response;

import java.util.List;

public record CartResponseDto(String id, List<FigureResponseDto> figures, int price) {
}
