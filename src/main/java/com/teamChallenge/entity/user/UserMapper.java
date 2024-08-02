package com.teamChallenge.entity.user;

import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.figure.FigureMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final FigureMapper figureMapper;

    public UserResponseDto toResponseDto(UserEntity user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getCreatedAt(),
                figureMapper.toResponseDtoList(user.getWhishList()),
                figureMapper.toResponseDtoList(user.getRecentlyViewed())
        );
    }

    public UserEntity toEntity(UserResponseDto userResponseDto) {
        return new UserEntity(
                userResponseDto.id(),
                userResponseDto.username(),
                userResponseDto.email(),
                null,
                userResponseDto.role(),
                userResponseDto.createdAt(),
                figureMapper.toEntityListFromResponse(userResponseDto.wishList()),
                figureMapper.toEntityListFromResponse(userResponseDto.recentlyViewed())
        );
    }

    public List<UserResponseDto> toResponseDtoList(List<UserEntity> users) {
        return users
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<UserEntity> toEntityListFromResponse(List<UserResponseDto> userResponseDto) {
        return userResponseDto.stream()
                .map(this::toEntity)
                .toList();
    }
}
