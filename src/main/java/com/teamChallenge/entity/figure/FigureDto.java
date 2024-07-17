package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.SubCategory;

import java.util.Date;
import java.util.List;

public record FigureDto(String id, String name, String shortDescription,
                        String longDescription, SubCategory subCategory, Category category,
                        int price, int amount, String color,
                        List<String> images, Date createdAt) {
    public FigureDto(String id, String name, String shortDescription,
                     String longDescription, SubCategory subCategory, int price,
                     int amount, String color, List<String> images, Date createdAt) {
        this(id, name, shortDescription, longDescription, subCategory, subCategory.getCategory(), price, amount, color, images, createdAt);
    }

    public FigureDto(String name, String shortDescription,
                     String longDescription, SubCategory subCategory, int price,
                     int amount, String color, List<String> images, Date createdAt) {
        this(null, name, shortDescription, longDescription, subCategory, subCategory.getCategory(), price, amount, color, images, createdAt);
    }
}
