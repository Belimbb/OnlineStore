package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.sections.Labels;

import java.util.List;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, CategoryResponseDto category, Labels label,
                                Boolean inWishList, int currentPrice, int oldPrice, int amount, String color,
                                List<String> images) {
    public FigureResponseDto(String id, String name, String shortDescription,
                     String longDescription, SubCategoryResponseDto subCategory, Labels label, Boolean inWishList, int currentPrice, int oldPrice,
                     int amount, String color, List<String> images) {
        this(id, name, shortDescription, longDescription, subCategory, subCategory.category(), label, inWishList, currentPrice, oldPrice, amount, color, images);
    }
}
