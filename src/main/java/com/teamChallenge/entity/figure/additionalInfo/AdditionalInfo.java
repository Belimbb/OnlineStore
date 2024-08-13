package com.teamChallenge.entity.figure.additionalInfo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {

    @NotBlank
    private String theme, material, characterName, productType, typeOfFigure, country, packageSize, toySize;
}
