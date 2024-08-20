package com.teamChallenge.entity.order;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.request.OrderRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;
import com.teamChallenge.dto.response.OrderResponseDto;
import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.order.delivery.DeliveryHistory;
import com.teamChallenge.entity.order.delivery.DeliveryStatuses;
import com.teamChallenge.entity.order.delivery.ReturnRequest;
import com.teamChallenge.entity.shoppingCart.CartServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomBadRequestException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.orderExceptions.ThisOrderCancelledException;
import com.teamChallenge.exception.exceptions.orderExceptions.UpdateDeliveryStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final CartServiceImpl cartService;
    private final FigureServiceImpl figureService;
    private final UserServiceImpl userService;

    public static final String OBJECT_NAME = "Order";

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
    public DeliveryStatuses[] getAllDeliveryStatuses() {
        DeliveryStatuses[] deliveryStatuses = DeliveryStatuses.values();
        log.info("{}: All delivery statuses retrieved from db", LogEnum.SERVICE);
        return deliveryStatuses;
    }

    @Override
    public OrderResponseDto create(OrderRequestDto orderRequestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userService.findByEmail(email);
        CartResponseDto cart = cartService.getById(orderRequestDto.cartId());

        List<FigureInCartOrderResponseDto> figureList = cart.figures();
        figureService.updatePurchaseCounter(figureList);

        OrderEntity newOrder = new OrderEntity(orderRequestDto.address(), figureList, currentUser.getId());
        orderRepository.save(newOrder);

        cartService.update(cart.id(), new CartRequestDto(null, null));
        log.info("{}: " + OBJECT_NAME + " was created", LogEnum.SERVICE);
        return orderMapper.toResponseDto(newOrder);
    }

    @Override
    public OrderResponseDto update(String id, OrderRequestDto orderRequestDto) {
        OrderEntity order = findById(id);

        if (order.getStatus().equals(Statuses.CANCELLED)) {
            throw new ThisOrderCancelledException(id);
        }

        order.setAddressInfo(orderRequestDto.address());

        List<FigureInCartOrderResponseDto> figureList = cartService.getById(orderRequestDto.cartId()).figures();
        figureService.updatePurchaseCounter(figureList);

        order.setStatus(orderRequestDto.status());
        order.setFigures(figureList);
        order.setTotalPrice();

        OrderEntity updatedOrder = orderRepository.save(order);
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, order.getId());
        return orderMapper.toResponseDto(updatedOrder);
    }

    @Override
    public OrderResponseDto updateDeliveryStatus(String orderId, String deliveryStatusStr) {
        OrderEntity order = findById(orderId);

        if (order.getStatus().equals(Statuses.CANCELLED)) {
            throw new ThisOrderCancelledException(orderId);
        }

        DeliveryStatuses deliveryStatus = getDeliveryStatusFromString(deliveryStatusStr);
        DeliveryHistory deliveryHistory = order.getDeliveryHistory();
        DeliveryHistory updatedDeliveryHistory;

        switch (deliveryStatus) {
            case PLACED_PROCESSED -> updatedDeliveryHistory = setPlacedProcessedDeliveryStatus(deliveryHistory);
            case SENT -> updatedDeliveryHistory = setSentDeliveryStatus(deliveryHistory);
            case DELIVERED -> {
                order.setStatus(Statuses.DELIVERED);
                updatedDeliveryHistory = setDeliveredDeliveryStatus(deliveryHistory);
            }
            case RETURN_REQUEST_PROCESSED -> updatedDeliveryHistory = setReturnRequestProcessedDeliveryStatus(deliveryHistory);
            case RETURNED -> {
                order.setStatus(Statuses.RETURNED);
                updatedDeliveryHistory = setReturnedDeliveryStatus(deliveryHistory);
            }
            case REFUNDED -> updatedDeliveryHistory = setRefundedDeliveryStatus(deliveryHistory);
            case FINISHED -> updatedDeliveryHistory = setFinishedDeliveryStatus(deliveryHistory);
            case CANCELLED -> {
                order.setStatus(Statuses.CANCELLED);
                updatedDeliveryHistory = setCancelledDeliveryStatus(deliveryHistory);
            }
            default -> throw new CustomBadRequestException(String.format("This delivery status (%s) is not used here.", deliveryStatusStr));
        }

        order.setDeliveryHistory(updatedDeliveryHistory);
        OrderEntity updatedOrder = orderRepository.save(order);
        log.info("{}: " + OBJECT_NAME + " (id: {}) delivery status (on: {}) updated)", LogEnum.SERVICE, orderId, deliveryStatus);
        return orderMapper.toResponseDto(updatedOrder);
    }

    @Override
    public OrderResponseDto submitReturnRequest(String orderId, String reason) {
        OrderEntity order = findById(orderId);
        DeliveryHistory deliveryHistory = order.getDeliveryHistory();

        order.setDeliveryHistory(setReturnRequestDeliveryStatus(deliveryHistory));
        order.setReturnRequest(new ReturnRequest(reason));

        OrderEntity updatedOrder = orderRepository.save(order);
        log.info("{}: Accepted a return request for an " + OBJECT_NAME + " (id: {}) ", LogEnum.SERVICE, orderId);
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

    private DeliveryStatuses getDeliveryStatusFromString(String deliveryStatusStr) {
        return DeliveryStatuses.valueOf(deliveryStatusStr);
    }

    private DeliveryHistory setPlacedProcessedDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isPlaced() && !deliveryHistory.isPlacedProcessed()) {
            deliveryHistory.setPlacedProcessed(true);
            deliveryHistory.setStatusDescription("we will send your order soon");
            deliveryHistory.setDateOfPlacedProcessed(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.PLACED_PROCESSED.name());
    }

    private DeliveryHistory setSentDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isPlacedProcessed() && !deliveryHistory.isSent()) {
            deliveryHistory.setSent(true);
            deliveryHistory.setStatusDescription("your order is on its way");
            deliveryHistory.setDateOfSent(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.SENT.name());
    }

    private DeliveryHistory setDeliveredDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isSent() && !deliveryHistory.isDelivered()) {
            deliveryHistory.setDelivered(true);
            deliveryHistory.setStatusDescription("confirm receipt of the order");
            deliveryHistory.setDateOfDelivered(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.DELIVERED.name());
    }

    private DeliveryHistory setReturnRequestDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isDelivered() && !deliveryHistory.isReturnRequested() && !deliveryHistory.isFinished()) {
            deliveryHistory.setReturnRequested(true);
            deliveryHistory.setStatusDescription("we are processing your return request");
            deliveryHistory.setDateOfReturnRequested(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.RETURN_REQUEST.name());
    }

    private DeliveryHistory setReturnRequestProcessedDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isReturnRequested() && !deliveryHistory.isReturnRequestProcessed()) {
            deliveryHistory.setReturnRequestProcessed(true);
            deliveryHistory.setStatusDescription("return your order");
            deliveryHistory.setDateOfReturnRequestProcessed(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.RETURN_REQUEST_PROCESSED.name());
    }

    private DeliveryHistory setReturnedDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isReturnRequestProcessed() && !deliveryHistory.isReturned()) {
            deliveryHistory.setReturned(true);
            deliveryHistory.setStatusDescription("you'll get your money back soon");
            deliveryHistory.setDateOfReturned(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.RETURNED.name());
    }

    private DeliveryHistory setRefundedDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isReturned() && !deliveryHistory.isRefunded()) {
            deliveryHistory.setRefunded(true);
            deliveryHistory.setStatusDescription("you'll get your money back soon");
            deliveryHistory.setDateOfRefunded(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.REFUNDED.name());
    }

    private DeliveryHistory setFinishedDeliveryStatus(DeliveryHistory deliveryHistory) {
        if ((deliveryHistory.isRefunded() || deliveryHistory.isDelivered()) && !deliveryHistory.isFinished()) {
            deliveryHistory.setFinished(true);
            deliveryHistory.setStatusDescription(null);
            deliveryHistory.setDateOfFinished(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.FINISHED.name());
    }

    private DeliveryHistory setCancelledDeliveryStatus(DeliveryHistory deliveryHistory) {
        if (deliveryHistory.isPlacedProcessed() && !deliveryHistory.isCancelled() && !deliveryHistory.isSent()) {
            deliveryHistory.setCancelled(true);
            deliveryHistory.setStatusDescription(null);
            deliveryHistory.setDateOfCancelled(new Date());
            return deliveryHistory;
        }

        throw new UpdateDeliveryStatusException(DeliveryStatuses.CANCELLED.name());
    }
}
