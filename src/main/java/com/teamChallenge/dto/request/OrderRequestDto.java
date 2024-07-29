package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(@NotBlank String address,
                              @NotNull String[] figuresId) {
}
