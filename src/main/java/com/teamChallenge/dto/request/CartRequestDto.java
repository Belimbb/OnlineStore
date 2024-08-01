package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CartRequestDto(@NotBlank String[] figuresId) {
}
