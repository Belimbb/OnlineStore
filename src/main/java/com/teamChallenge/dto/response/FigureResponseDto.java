package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.sections.Labels;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, Labels label,
                                int currentPrice, int oldPrice, int amount, List<String> images) {
}
