package com.teamChallenge.entity.Figures;

import java.util.Date;
import java.util.List;

public record FigureDto(String id, String name, String shortDescription,
                        String longDescription, Enum category,
                        int price, int amount, String color,
                        List<String> images, Date createdA) {

}
