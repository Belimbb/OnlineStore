package com.teamChallenge.entity.Figures;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FigureDto {
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private int price;

    @NotNull
    private int amount;

    @NotNull
    private String color;

    @NotNull
    private List<String> images;

    @NotNull
    private Date createdAt;
}
