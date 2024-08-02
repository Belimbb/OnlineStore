package com.teamChallenge.dto.response;

import com.teamChallenge.entity.user.Roles;

import java.util.Date;
import java.util.List;

public record UserResponseDto(String id,
                              String email,
                              String username,
                              String password,
                              Roles role,
                              Date createdAt,
                              List<FigureResponseDto> wishList,
                              List<FigureResponseDto> recentlyViewed) {
}
