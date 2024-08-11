package com.teamChallenge.entity.order;

import com.teamChallenge.dto.request.OrderRequestDto;
import com.teamChallenge.dto.response.OrderResponseDto;
import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final FigureServiceImpl figureService;

    private final UserServiceImpl userService;

    private static final String OBJECT_NAME = "Order";

    @Override
    public List<OrderResponseDto> getAll() {
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        List<OrderEntity> all = orderRepository.findAll();
        return orderMapper.toResponseDtoList(all);
    }

    @Override
    public OrderResponseDto getById(String id) {
        log.info("{}: " + OBJECT_NAME + " (id: {}) retrieved from db", LogEnum.SERVICE, id);
        return orderMapper.toResponseDto(findById(id));
    }

    @Override
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);

        List<FigureInCartOrderResponseDto> figureList = figureService.getCartOrderResponseFigures(orderRequestDto.figures());

        OrderEntity newOrder = new OrderEntity(orderRequestDto.address(), figureList, currentUser.getId());
        orderRepository.save(newOrder);
        log.info("{}: " + OBJECT_NAME + " was created", LogEnum.SERVICE);
        return orderMapper.toResponseDto(newOrder);
    }

    @Override
    public OrderResponseDto update(String id, OrderRequestDto orderRequestDto) {
        OrderEntity order = findById(id);
        order.setAddressInfo(orderRequestDto.address());

        List<FigureInCartOrderResponseDto> figureList = figureService.getCartOrderResponseFigures(orderRequestDto.figures());
        order.setStatus(orderRequestDto.status());
        order.setFigures(figureList);
        order.setTotalPrice();

        OrderEntity updatedOrder = orderRepository.save(order);
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, order.getId());
        return orderMapper.toResponseDto(updatedOrder);
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
