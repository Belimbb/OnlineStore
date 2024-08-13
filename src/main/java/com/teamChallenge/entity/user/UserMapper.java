package com.teamChallenge.entity.user;

import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.figure.FigureMapper;
import com.teamChallenge.entity.order.OrderMapper;
import com.teamChallenge.entity.review.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final FigureMapper figureMapper;
    private final ReviewMapper reviewMapper;
    private final OrderMapper orderMapper;

    public UserResponseDto toResponseDto(UserEntity entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole(),
                entity.getAddressInfo(),
                entity.getCreatedAt(),
                figureMapper.toResponseDtoList(entity.getWhishList()),
                figureMapper.toResponseDtoList(entity.getRecentlyViewed()),
                reviewMapper.toResponseDtoList(entity.getReviews()),
                entity.getOrderHistory() == null ? new ArrayList<>() : orderMapper.toResponseDtoList(entity.getOrderHistory()),
                entity.isAccountVerified()
        );
    }

    public UserEntity toEntity(UserResponseDto dto) {
        return new UserEntity(
                dto.id(),
                dto.username(),
                dto.email(),
                dto.phoneNumber(),
                null,
                dto.role(),
                dto.addressInfo(),
                dto.createdAt(),
                reviewMapper.toEntityListFromResponse(dto.reviewResponseDtoList()),
                figureMapper.toEntityListFromResponse(dto.wishList()),
                figureMapper.toEntityListFromResponse(dto.recentlyViewed()),
                orderMapper.toEntityListFromResponse(dto.orderResponseHistory()),
                dto.isAccountVerified(),
                null
        );
    }

    public List<UserResponseDto> toResponseDtoList(List<UserEntity> entities) {
        return entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<UserEntity> toEntityListFromResponse(List<UserResponseDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }
}
