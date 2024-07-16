package com.teamChallenge.entity.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> getAll() {
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public OrderDto getById(String id) {
        return orderMapper.toDto(orderRepository.findById(id).get());
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        OrderEntity newOrder = new OrderEntity(orderDto.address(), orderDto.price(), orderDto.figureList());
        orderRepository.save(newOrder);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto update(String id, OrderDto orderDto) {
        OrderEntity order = orderRepository.findById(id).get();
        order.setAddress(orderDto.address());
        order.setPrice(orderDto.price());
        order.setFigureList(orderDto.figureList());

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public boolean delete(String id) {
        orderRepository.deleteById(id);
        return true;
    }
}