package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureMapper;
import com.teamChallenge.exception.LogEnum;

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

    @Override
    public List<OrderDto> getAll() {
        log.info("{}: All orders retrieved from db", LogEnum.SERVICE);
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public OrderDto getById(String id) {
        log.info("{}: Order (id: {}) retrieved from db", LogEnum.SERVICE, id);
        return orderMapper.toDto(orderRepository.findById(id).get());
    }

    @Override
    public OrderDto create(String address, int price, List<FigureEntity> figureList) {
        OrderEntity newOrder = new OrderEntity(address, price, figureList);
        orderRepository.save(newOrder);
        log.info("{}: Order was created", LogEnum.SERVICE);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        return create(orderDto.address(), orderDto.price(), figureMapper.toEntityList(orderDto.figureList()));
    }

    @Override
    public OrderDto update(String id, OrderDto orderDto) {
        OrderEntity order = orderRepository.findById(id).get();
        order.setAddress(orderDto.address());
        order.setPrice(orderDto.price());
        order.setFigureList(figureMapper.toEntityList(orderDto.figureList()));

        orderRepository.save(order);
        log.info("{}: Order (id: {}) updated)", LogEnum.SERVICE, order.getId());
        return orderMapper.toDto(order);
    }

    @Override
    public void delete(String id) {
        if(orderRepository.existsById(id)){
            orderRepository.deleteById(id);
            log.info("{}: Order (id: {}) deleted", LogEnum.SERVICE, id);
        }
    }
}
