package com.teamChallenge.entity.Figures;

import com.teamChallenge.entity.Products.Category;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record FigureDto(UUID id, String name, String shortDescription,
                        String longDescription, Category category,
                        int price, int amount, String color,
                        List<String> images, Date createdA) {

}
