package com.teamChallenge.dto.request;

import com.teamChallenge.dto.response.FigureResponseDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CartRequestDto(@NotBlank List<FigureResponseDto> figures,
                             @NotBlank String userId) {
}
