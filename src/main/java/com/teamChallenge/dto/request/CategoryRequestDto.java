package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(@NotBlank String name) {
}
