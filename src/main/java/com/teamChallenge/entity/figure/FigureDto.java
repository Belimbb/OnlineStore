package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.SubCategory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public record FigureDto(String id, String name, String shortDescription,
                        String longDescription, SubCategory subCategory, Category category, Labels label,
                        Boolean inWishList, int currentPrice, int oldPrice, int amount, String color,
                        List<String> images, Date createdAt) {
    public FigureDto(String id, String name, String shortDescription,
                     String longDescription, SubCategory subCategory, Labels label, Boolean inWishList, int currentPrice, int oldPrice,
                     int amount, String color, List<String> images, Date createdAt) {
        this(id, name, shortDescription, longDescription, subCategory, subCategory.getCategory(), label, inWishList, currentPrice, oldPrice, amount, color, images, createdAt);
    }

    public FigureDto(String name, String shortDescription,
                     String longDescription, SubCategory subCategory, int currentPrice, int oldPrice,
                     int amount, String color, List<String> images, Date createdAt) {
        this(null, name, shortDescription, longDescription, subCategory, subCategory.getCategory(), null, null, currentPrice, oldPrice, amount, color, images, createdAt);
    }
}
