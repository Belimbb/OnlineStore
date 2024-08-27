package com.teamChallenge.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record ReviewRequestDto(
        @NotBlank String figureId,
        @NotNull @Min(0) @Max(5) byte score,
        @NotBlank String text,
        @NotBlank String advantages,
        @NotBlank String disadvantages,
        List<String> videos, List<String> photos) {
}
