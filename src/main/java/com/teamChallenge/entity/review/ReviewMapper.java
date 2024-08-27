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
                review.getText(),
                review.getAdvantages(),
                review.getDisadvantages(),
                review.getVideos(),
                review.getPhotos(),
                review.getReplies(),
                review.getLikes(),
                review.getLikes()
        );
    }

    public ReviewEntity toEntity(ReviewResponseDto dto) {
        return new ReviewEntity(
                dto.id(),
                dto.score(),
                null,
                dto.creationDate(),
                dto.text(),
                dto.advantages(),
                dto.disadvantages(),
                dto.videos(),
                dto.photos(),
                dto.replies(),
                dto.likes(),
                dto.dislikes(),
                null
                );
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
