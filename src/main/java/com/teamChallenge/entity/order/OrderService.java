package com.teamChallenge.entity.order;

import com.teamChallenge.dto.request.OrderRequestDto;
import com.teamChallenge.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAll();

    OrderResponseDto getById(String id);

    OrderResponseDto create(OrderRequestDto orderRequestDto);

    OrderResponseDto update(String id, OrderRequestDto orderRequestDto);

    void delete(String id);
}