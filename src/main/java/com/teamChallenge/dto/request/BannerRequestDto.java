package com.teamChallenge.dto.request;


import jakarta.validation.constraints.NotBlank;

public record BannerRequestDto(@NotBlank String title,
                               @NotBlank String description,
                               @NotBlank String collectionName,
                               @NotBlank String imageName) {
}
