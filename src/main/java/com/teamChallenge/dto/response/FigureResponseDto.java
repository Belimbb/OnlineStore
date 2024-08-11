package com.teamChallenge.dto.response;

import com.teamChallenge.entity.figure.additionalInfo.AdditionalInfo;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.Types;

import java.util.List;
import java.util.Map;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, Labels label, Types type,
                                int currentPrice, int oldPrice, int amount, AdditionalInfo additionalInfo, List<String> images, List<ReviewResponseDto> reviewResponseDtoList,
                                double averageRating, Map<Byte, Integer> ratingDistribution) {
}
