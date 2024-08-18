package com.teamChallenge.dto.response;

import com.teamChallenge.entity.review.reply.Reply;

import java.util.Date;
import java.util.List;

public record ReviewResponseDto(String id, byte score, Date creationDate,
                                String text, String advantages, String disadvantages,
                                List<String> videos, List<String> photos,
                                List<Reply> replies, int likes, int dislikes) {
}
