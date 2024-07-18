package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureEntity;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAll();

    OrderDto getById(String id);

    OrderDto create (String address, int price, List<FigureEntity> figureList);
    OrderDto create(OrderDto orderDto);

    OrderDto update(String id, OrderDto orderDto);

    void delete(String id);
}