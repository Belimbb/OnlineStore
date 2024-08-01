package com.teamChallenge.dto.request.figure;

import jakarta.validation.constraints.NotBlank;

public record FigureInOrderRequestDto(@NotBlank String id,
                                      @NotBlank int amount) {
}
