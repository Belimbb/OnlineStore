package com.teamChallenge.dto.response.figure;

import com.teamChallenge.dto.response.ReviewResponseDto;
import com.teamChallenge.dto.response.SubCategoryResponseDto;
import com.teamChallenge.entity.figure.additionalInfo.AdditionalInfo;
import com.teamChallenge.entity.figure.sections.Labels;

import java.util.List;
import java.util.Map;

public record FigureResponseDto(String id, String name, String shortDescription,
                                String longDescription, SubCategoryResponseDto subCategory, Labels label,
                                int currentPrice, int oldPrice, int amount, AdditionalInfo additionalInfo, List<String> images, List<ReviewResponseDto> reviewResponseDtoList,
                                double averageRating, Map<Byte, Integer> ratingDistribution) {
}
