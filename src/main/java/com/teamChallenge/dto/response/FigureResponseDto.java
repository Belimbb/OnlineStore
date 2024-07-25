package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.SubCategory;

import java.util.List;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategory subCategory, Category category, Labels label,
                                Boolean inWishList, int currentPrice, int oldPrice, int amount, String color,
                                List<String> images) {
    public FigureResponseDto(String id, String name, String shortDescription,
                     String longDescription, SubCategory subCategory, Labels label, Boolean inWishList, int currentPrice, int oldPrice,
                     int amount, String color, List<String> images) {
        this(id, name, shortDescription, longDescription, subCategory, subCategory.getCategory(), label, inWishList, currentPrice, oldPrice, amount, color, images);
    }
}
