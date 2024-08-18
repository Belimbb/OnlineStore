package com.teamChallenge.entity.review;

import com.teamChallenge.dto.response.ReviewResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ReviewResponseDto> toResponseDtoList(List<ReviewEntity> entities) {
        return entities == null ? null : entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<ReviewEntity> toEntityListFromResponse(List<ReviewResponseDto> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
