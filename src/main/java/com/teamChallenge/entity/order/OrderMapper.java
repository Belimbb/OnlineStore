package com.teamChallenge.entity.order;

import com.teamChallenge.dto.response.OrderResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderMapper {

    public OrderResponseDto toResponseDto(OrderEntity entity) {
        return new OrderResponseDto(
                entity.getId(),
                entity.getAddressInfo(),
                entity.getTotalPrice(),
                entity.getStatus(),
                entity.getFigures(),
                entity.getUserId(),
                entity.getDateOfCompletion()
        );
    }

    public OrderEntity toEntity(OrderResponseDto dto) {
        return new OrderEntity(
                dto.id(),
                dto.address(),
                dto.totalPrice(),
                dto.status(),
                dto.figureList(),
                dto.userId(),
                dto.dateOfCompletion()
        );
    }

    public List<OrderResponseDto> toResponseDtoList(List<OrderEntity> entities) {
        return entities == null ? null : entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<OrderEntity> toEntityListFromResponse(List<OrderResponseDto> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
