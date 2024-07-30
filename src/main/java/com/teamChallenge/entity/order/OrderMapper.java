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

    public OrderResponseDto toResponseDto(OrderEntity order) {
        return new OrderResponseDto(
                order.getId(),
                order.getAddress(),
                order.getPrice(),
                order.getStatus(),
                figureMapper.toResponseDtoList(order.getFigureList()),
                userMapper.toResponseDto(order.getUser()));
    }

    public OrderEntity toEntity(OrderResponseDto orderResponseDto) {
        return new OrderEntity(
                orderResponseDto.id(),
                orderResponseDto.address(),
                orderResponseDto.price(),
                orderResponseDto.status(),
                figureMapper.toEntityListFromResponse(orderResponseDto.figureList()),
                userMapper.toEntity(orderResponseDto.userResponseDto()));
    }

    public List<OrderResponseDto> toResponseDtoList(List<OrderEntity> orders) {
        return orders
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<OrderEntity> toEntityListFromResponse(List<OrderResponseDto> orderResponseDto) {
        return orderResponseDto.stream()
                .map(this::toEntity)
                .toList();
    }
}
