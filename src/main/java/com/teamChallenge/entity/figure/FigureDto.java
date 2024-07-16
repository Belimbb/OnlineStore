package com.teamChallenge.entity.figure;

import java.util.Date;
import java.util.List;

public record FigureDto(String id, String name, String shortDescription,
                        String longDescription, Enum<?> subCategory, String category,
                        int price, int amount, String color,
                        List<String> images, Date createdAt) {
    public FigureDto(String id, String name, String shortDescription,
                     String longDescription, Enum<?> subCategory, int price,
                     int amount, String color, List<String> images, Date createdAt) {
        this(id, name, shortDescription, longDescription, subCategory, subCategory.getClass().getSimpleName(), price, amount, color, images, createdAt);
    }

    public FigureDto(String name, String shortDescription,
                     String longDescription, Enum<?> subCategory, int price,
                     int amount, String color, List<String> images, Date createdAt) {
        this(null, name, shortDescription, longDescription, subCategory, subCategory.getClass().getSimpleName(), price, amount, color, images, createdAt);
    }
}
