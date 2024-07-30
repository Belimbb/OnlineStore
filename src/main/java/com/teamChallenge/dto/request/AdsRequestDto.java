package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AdsRequestDto(@NotBlank String text,
                            @NotBlank String url) {
}
