package com.teamChallenge.entity.Figures;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record FigureDto(UUID id, String name, String shortDescription,
                        String longDescription, Enum<?> subCategory, String category,
                        int price, int amount, String color,
                        List<String> images, Date createdAt) {

    public FigureDto(UUID id, String name, String shortDescription,
                     String longDescription, Enum<?> subCategory, int price,
                     int amount, String color, List<String> images, Date createdAt) {
        this(id, name, shortDescription, longDescription, subCategory, subCategory.getClass().getSimpleName(), price, amount, color, images, createdAt);
    }
}

