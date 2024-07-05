package com.teamChallenge.entity.Orders;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getAll();

    OrderDto getById(UUID id);

    OrderDto create(OrderDto orderDto);

    OrderDto update(UUID id, OrderDto orderDto);

    boolean delete(UUID id);
}
