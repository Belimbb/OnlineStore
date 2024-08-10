package com.teamChallenge.entity.order;

import com.teamChallenge.dto.response.OrderResponseDto;
import com.teamChallenge.entity.figure.FigureMapper;
import com.teamChallenge.entity.user.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class OrderMapper {

    private final FigureMapper figureMapper;

    private final UserMapper userMapper;

    public OrderResponseDto toResponseDto(OrderEntity entity) {
        return new OrderResponseDto(
                entity.getId(),
                entity.getAddressInfo(),
                entity.getPrice(),
                entity.getStatus(),
                figureMapper.toResponseDtoList(entity.getFigureList()),
                userMapper.toResponseDto(entity.getUser()));
    }

    public OrderEntity toEntity(OrderResponseDto dto) {
        return new OrderEntity(
                dto.id(),
                dto.address(),
                dto.price(),
                dto.status(),
                figureMapper.toEntityListFromResponse(dto.figureList()),
                userMapper.toEntity(dto.userResponseDto()));
    }

    public List<OrderResponseDto> toResponseDtoList(List<OrderEntity> entities) {
        return entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<OrderEntity> toEntityListFromResponse(List<OrderResponseDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }
}
