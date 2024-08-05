package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.sections.Labels;

import java.util.List;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, Labels label,
                                int currentPrice, int oldPrice, int amount, List<String> images,
                                String theme, String material, String characterName, String productType,
                                String typeOfFigure, String country, String packageSize, String toySize, List<ReviewResponseDto> reviewResponseDtoList) {
}
