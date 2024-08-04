package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.sections.Labels;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, Labels label,
                                int currentPrice, int oldPrice, int amount,
                                Map<String, String> basicCharacteristics, Map<String, String> dimensions,
                                List<String> images) {
    public FigureResponseDto(String id, String name, String shortDescription, String longDescription,
                             SubCategoryResponseDto subCategory, Labels label,
                             int currentPrice, int oldPrice, int amount,
                             //basicCharacteristics
                             String theme, String material, String characterName,
                             String productType, String typeOfFigure, String country,
                             //dimensions
                             String packageSize, String toySize,
                             List<String> images) {
        this(id, name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount,
                //basicCharacteristics
                Map.of("Theme", theme, "Material", material, "Character name", characterName,
                        "Product type", productType, "Type of figure", typeOfFigure, "Country", country),
                //dimensions
                Map.of("Package size", packageSize, "Toy size", toySize),
                images);
    }
}
