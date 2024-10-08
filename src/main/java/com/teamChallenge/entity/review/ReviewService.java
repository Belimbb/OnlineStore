package com.teamChallenge.entity.review;

import com.teamChallenge.dto.request.ReplyRequestDto;
import com.teamChallenge.dto.request.ReviewRequestDto;
import com.teamChallenge.dto.response.ReviewResponseDto;
import com.teamChallenge.entity.review.reply.Reply;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDto> getAll();

    ReviewResponseDto getById(String id);

    ReviewResponseDto create(ReviewRequestDto reviewRequestDto);

    ReviewResponseDto addReply(String reviewId, ReplyRequestDto reply);

    ReviewResponseDto update(String id, ReviewRequestDto reviewRequestDto);

    void delete(String id);

    void deleteReply(String reviewId, ReplyRequestDto reply);
}