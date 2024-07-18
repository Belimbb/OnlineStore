package com.teamChallenge.request;

import com.teamChallenge.entity.figure.sections.SubCategory;

import java.util.List;

public record FigureRequest(String name, String shortDescription,
                            String longDescription, SubCategory subCategory,
                            int price, int amount, String color, List<String> images) {
}
