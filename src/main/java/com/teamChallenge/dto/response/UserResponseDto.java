package com.teamChallenge.dto.response;

import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.address.AddressInfo;

import java.util.Date;
import java.util.List;

public record UserResponseDto(String id,
                              String email,
                              String phoneNumber,
                              String username,
                              String password,
                              Roles role,
                              AddressInfo addressInfo,
                              Date createdAt,
                              List<FigureResponseDto> wishList,
                              List<FigureResponseDto> recentlyViewed,
                              List<ReviewResponseDto> reviewResponseDtoList) {
}
