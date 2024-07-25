package com.teamChallenge.dto.response;

import com.teamChallenge.entity.user.Roles;

import java.util.List;

public record UserResponseDto(String id,
                              String email,
                              String username,
                              String password, Roles role,
                              CartResponseDto cart,
                              List<FigureResponseDto> wishList) {
}
