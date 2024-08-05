package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewRequestDto(
        @NotBlank String figureId,
        @NotBlank @Size(max = 5) byte score,
        @NotBlank String advantages,
        @NotBlank String disadvantages) {
}
