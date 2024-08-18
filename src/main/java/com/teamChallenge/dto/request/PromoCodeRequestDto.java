package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record PromoCodeRequestDto(@NotBlank String code,
                                  @NotNull int discount,
                                  @NotNull Date expirationDate) {
}
