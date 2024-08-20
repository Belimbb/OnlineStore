package com.teamChallenge.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BannerRequestDto(@NotBlank String title,
                               @NotBlank String description,
                               @NotBlank String collectionName,
                               @NotBlank String imageName,
                               @Size(min = 7, max = 7) @Schema(example = "#E44C90") String textColor,
                               @Size(min = 7, max = 7) @Schema(example = "#153BB8") String collectionNameColor,
                               @Size(min = 7, max = 7) @Schema(example = "#F7EE02") String buttonColor) {
}
