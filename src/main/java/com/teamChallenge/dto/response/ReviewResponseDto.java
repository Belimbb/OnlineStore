package com.teamChallenge.dto.response;

import java.util.Date;

public record ReviewResponseDto(String id, byte score, Date creationDate,
                                String advantages, String disadvantages) {
}
