package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class OrderMapper {
    private final FigureMapper figureMapper;

    public OrderDto toDto(OrderEntity order) {
        return new OrderDto(
                order.getId(),
                order.getAddress(),
                order.getPrice(),
                order.getStatus(),
                order.getFigureList(),
                order.getUser());
    }

    public OrderEntity toEntity(OrderDto orderDto) {
        return new OrderEntity(
                orderDto.id(),
                orderDto.address(),
                orderDto.price(),
                orderDto.status(),
                orderDto.figureList(),
                orderDto.user());
    }

    public List<OrderDto> toDtoList(List<OrderEntity> orders) {
        return orders
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<OrderEntity> toEntityList(List<OrderDto> orderDtos) {
        return orderDtos
                .stream()
                .map(this::toEntity)
                .toList();
    }
}
