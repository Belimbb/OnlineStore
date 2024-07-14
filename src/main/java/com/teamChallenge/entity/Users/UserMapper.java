package com.teamChallenge.entity.Users;

import com.teamChallenge.entity.ShoppingCart.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CartMapper cartMapper;

    public UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getCreatedAt(),
                cartMapper.toDto(user.getCart()));
    }

    public UserEntity toEntity(UserDto userDto) {
        return new UserEntity(
                userDto.id(),
                userDto.username(),
                userDto.email(),
                userDto.password(),
                userDto.role(),
                userDto.createdAt());
    }

    public List<UserDto> toDtoList(List<UserEntity> users) {
        return users
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<UserEntity> toEntityList(List<UserDto> userDtos) {
        return userDtos
                .stream()
                .map(this::toEntity)
                .toList();
    }
}