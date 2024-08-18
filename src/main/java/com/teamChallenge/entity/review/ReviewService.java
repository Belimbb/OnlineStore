package com.teamChallenge.entity.review;

import com.teamChallenge.dto.request.ReviewRequestDto;
import com.teamChallenge.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDto> getAll();

    ReviewResponseDto getById(String id);

    ReviewResponseDto create(ReviewRequestDto reviewRequestDto);

    ReviewResponseDto update(String id, ReviewRequestDto reviewRequestDto);

    void delete(String id);
}