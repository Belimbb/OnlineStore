package com.teamChallenge.entity.order;

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
    public OrderDto create(OrderDto orderDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);

        List<FigureEntity> figureList = orderDto.figureList()
                .stream()
                .map(figure -> figureService.findById(figure.getId()))
                .toList();

        int totalPrice = figureList
                .stream()
                .mapToInt(FigureEntity::getCurrentPrice)
                .sum();

        OrderEntity newOrder = new OrderEntity(orderDto.address(), totalPrice, figureList, currentUser);
        orderRepository.save(newOrder);
        log.info("{}: " + OBJECT_NAME + " was created", LogEnum.SERVICE);
        return orderMapper.toDto(newOrder);
    }

    @Override
    public OrderDto update(String id, OrderDto orderDto) {
        OrderEntity order = findById(id);

        if (orderDto.address() != null && !orderDto.address().isBlank()) {
            order.setAddress(orderDto.address());
        }

        List<FigureEntity> figureList = orderDto.figureList()
                .stream()
                .map(figure -> figureService.findById(figure.getId()))
                .toList();
        order.setFigureList(figureList);

        int totalPrice = figureList
                .stream()
                .mapToInt(FigureEntity::getCurrentPrice)
                .sum();
        order.setPrice(totalPrice);

        OrderEntity updatedOrder = orderRepository.save(order);
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, order.getId());
        return orderMapper.toDto(updatedOrder);
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
