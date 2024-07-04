package com.teamChallenge.entity.Orders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public OrderDto getById(UUID id) {
        return orderMapper.toDto(orderRepository.getReferenceById(id));
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        OrderEntity newOrder = new OrderEntity(orderDto.address(), orderDto.price(), orderDto.products());
        orderRepository.save(newOrder);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto update(UUID id, OrderDto orderDto) {
        OrderEntity order = orderRepository.getReferenceById(id);
        order.setAddress(orderDto.address());
        order.setPrice(orderDto.price());
        order.setProducts(orderDto.products());

        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public boolean delete(UUID id) {
        orderRepository.deleteById(id);
        return true;
    }
}
