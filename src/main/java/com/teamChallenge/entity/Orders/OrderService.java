package com.teamChallenge.entity.Orders;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAll();

    OrderDto getById(String id);

    OrderDto create(OrderDto orderDto);

    OrderDto update(String id, OrderDto orderDto);

    boolean delete(String id);
}
