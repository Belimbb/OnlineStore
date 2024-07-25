package com.teamChallenge.dto.request;

import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.SubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record FigureRequestDto(@NotBlank @Size(max = 50) String name,
                               @NotBlank @Size(min = 10, max = 200) String shortDescription,
                               @NotBlank @Size(min = 10, max = 1000) String longDescription,
                               @NotNull SubCategory subCategory,
                               Labels label,
                               @NotNull int currentPrice,
                               @NotNull int oldPrice,
                               @NotNull int amount,
                               @NotBlank @Size(min = 7, max = 7) String color,
                               List<String> images) {
}