package com.teamChallenge.entity.Orders;

import com.teamChallenge.exception.exceptions.orderExceptions.OrderNotFoundException;
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
        return orderMapper.toDto(findById(id));
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        OrderEntity newOrder = new OrderEntity(orderDto.address(), orderDto.price(), orderDto.figureList());
        orderRepository.save(newOrder);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto update(String id, OrderDto orderDto) {
        OrderEntity order = findById(id);
        order.setAddress(orderDto.address());
        order.setPrice(orderDto.price());
        order.setFigureList(orderDto.figureList());

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public boolean delete(String id) {
        OrderEntity order = findById(id);
        orderRepository.delete(order);
        return true;
    }

    private OrderEntity findById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
}
