package com.teamChallenge.request;

import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.SubCategory;

import java.util.List;

public record FigureRequest(String name, String shortDescription,
                            String longDescription, SubCategory subCategory, Labels label,
                            int currentPrice, int oldPrice, int amount, String color, List<String> images) {
}
