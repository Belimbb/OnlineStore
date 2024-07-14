package com.teamChallenge.request;

import java.util.List;

public record FigureRequest(String name, String shortDescription,
                            String longDescription, Enum<?> subCategory,
                            int price, int amount, String color, List<String> images) {
}
