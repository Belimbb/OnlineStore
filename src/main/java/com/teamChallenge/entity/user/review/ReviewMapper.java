package com.teamChallenge.entity.user.review;

import com.teamChallenge.dto.response.ReviewResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ReviewMapper {

    public ReviewResponseDto toResponseDto(ReviewEntity review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getScore(),
                review.getCreationDate(),
                review.getAdvantages(),
                review.getDisadvantages());
    }

    public ReviewEntity toEntity(ReviewResponseDto dto) {
        return new ReviewEntity(
                dto.id(),
                dto.score(),
                null,
                dto.creationDate(),
                dto.advantages(),
                dto.disadvantages(),
                null);
    }

    public List<ReviewResponseDto> toResponseDtoList(List<ReviewEntity> reviewList) {
        return reviewList == null ? null : reviewList
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<ReviewEntity> toEntityListFromResponse(List<ReviewResponseDto> dtoList) {
        return dtoList
                .stream()
                .map(this::toEntity)
                .toList();
    }
}
