package com.teamChallenge.dto.request.figure;

import jakarta.validation.constraints.NotBlank;

public record FigureInCartOrderRequestDto(@NotBlank String id,
                                          @NotBlank int amount) {
}
