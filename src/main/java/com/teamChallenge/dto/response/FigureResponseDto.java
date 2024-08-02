package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.sections.Labels;

import java.util.List;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, Labels label,
                                Boolean inWishList, int currentPrice, int oldPrice, int amount, String color,
                                List<String> images) {
}
