package com.teamChallenge.dto.request;

import jakarta.validation.constraints.*;

public record ReviewRequestDto(
        @NotBlank String figureId,
        @NotNull @Min(0) @Max(5) byte score,
        @NotBlank String advantages,
        @NotBlank String disadvantages) {
}
