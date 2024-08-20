package com.teamChallenge.entity.order;

import com.teamChallenge.dto.request.OrderRequestDto;
import com.teamChallenge.dto.response.OrderResponseDto;
import com.teamChallenge.entity.order.delivery.DeliveryStatuses;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAll();

    OrderResponseDto getById(String id);

    DeliveryStatuses[] getAllDeliveryStatuses();

    OrderResponseDto create(OrderRequestDto orderRequestDto);

    OrderResponseDto update(String id, OrderRequestDto orderRequestDto);

    OrderResponseDto updateDeliveryStatus(String orderId, String deliveryStatusStr);

    OrderResponseDto submitReturnRequest(String orderId, String reason);

    void delete(String id);
}