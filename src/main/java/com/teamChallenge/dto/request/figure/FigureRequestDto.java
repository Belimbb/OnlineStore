package com.teamChallenge.dto.request.figure;

import com.teamChallenge.entity.figure.sections.Labels;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record FigureRequestDto(@NotBlank @Size(max = 50) String name,
                               @NotBlank @Size(min = 10, max = 200) String shortDescription,
                               @NotBlank @Size(min = 10, max = 1000) String longDescription,
                               @NotNull String subCategoryName,
                               Labels label,
                               @NotNull int currentPrice,
                               @NotNull int oldPrice,
                               @NotNull int amount,
                               List<String> images,
                               @NotBlank String theme,
                               @NotBlank String material,
                               @NotBlank String characterName,
                               @NotBlank String productType,
                               @NotBlank String typeOfFigure,
                               @NotBlank String country,
                               @NotBlank String packageSize,
                               @NotBlank String toySize) {
}