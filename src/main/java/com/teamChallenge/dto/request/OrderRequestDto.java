package com.teamChallenge.dto.request;

import com.teamChallenge.dto.response.FigureResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDto(@NotBlank String address,
                              @NotNull List<FigureResponseDto> figureList) {
}
