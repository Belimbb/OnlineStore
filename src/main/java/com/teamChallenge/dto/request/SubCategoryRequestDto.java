package com.teamChallenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SubCategoryRequestDto(@NotBlank String subCategoryName,
                                    @NotBlank String categoryName) {
}
