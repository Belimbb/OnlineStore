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

    public UserResponseDto toResponseDto(UserEntity entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole(),
                entity.getCreatedAt(),
                figureMapper.toResponseDtoList(entity.getWhishList()),
                figureMapper.toResponseDtoList(entity.getRecentlyViewed())
        );
    }

    public UserEntity toEntity(UserResponseDto dto) {
        return new UserEntity(
                dto.id(),
                dto.username(),
                dto.email(),
                null,
                dto.role(),
                dto.createdAt(),
                figureMapper.toEntityListFromResponse(dto.wishList()),
                figureMapper.toEntityListFromResponse(dto.recentlyViewed())
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
