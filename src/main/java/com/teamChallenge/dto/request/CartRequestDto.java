package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CartRequestDto(@NotBlank List<FigureRequestDto> figures) {
}
