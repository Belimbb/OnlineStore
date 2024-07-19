package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureMapper;
import com.teamChallenge.exception.LogEnum;

import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
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

    private final FigureMapper figureMapper;

    private static final String OBJECT_NAME = "Order";

    @Override
    public List<OrderDto> getAll() {
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public OrderDto getById(String id) {
        log.info("{}: " + OBJECT_NAME + " (id: {}) retrieved from db", LogEnum.SERVICE, id);
        return orderMapper.toDto(findById(id));
    }

    @Override
    public OrderDto create(String address, int price, List<FigureEntity> figureList) {
        OrderEntity newOrder = new OrderEntity(address, price, figureList);
        orderRepository.save(newOrder);
        log.info("{}: " + OBJECT_NAME + " was created", LogEnum.SERVICE);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        return create(orderDto.address(), orderDto.price(), figureMapper.toEntityList(orderDto.figureList()));
    }

    @Override
    public OrderDto update(OrderDto orderDto) {
        OrderEntity order = orderRepository.save(orderMapper.toEntity(orderDto));
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, order.getId());
        return orderMapper.toDto(order);
    }

    @Override
    public void delete(String id) {
        OrderEntity order = findById(id);
        orderRepository.delete(order);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }

    private OrderEntity findById(String id) {
        return orderRepository.findById(id).orElseThrow(()-> new CustomNotFoundException(OBJECT_NAME, id));
    }
}
