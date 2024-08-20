package com.teamChallenge.controller;

import com.teamChallenge.dto.request.OrderRequestDto;
import com.teamChallenge.dto.response.OrderResponseDto;
import com.teamChallenge.entity.order.OrderService;
import com.teamChallenge.entity.order.delivery.DeliveryStatuses;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";

    private final OrderService orderService;

    @PostMapping
    @SecurityRequirement(name = SEC_REC)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    public OrderResponseDto create(@Valid @RequestBody OrderRequestDto orderDto) {
        OrderResponseDto order = orderService.create(orderDto);
        log.info("{}: Order (id: {}) has been added", LogEnum.CONTROLLER, order.id());
        return order;
    }

    @GetMapping
    @Operation(description = "get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))}
            )
    })
    public List<OrderResponseDto> getAll() {
        List<OrderResponseDto> orderList = orderService.getAll();
        log.info("{}: Order list has been retrieved", LogEnum.CONTROLLER);
        return orderList;
    }

    @GetMapping(URI_WITH_ID)
    @Operation(description = "get an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public OrderResponseDto getById(@PathVariable String id) {
        OrderResponseDto order = orderService.getById(id);
        log.info("{}: Order (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return order;
    }

    @GetMapping("/delivery-statuses")
    @Operation(description = "get all delivery statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of delivery status")
    })
    public DeliveryStatuses[] getAllDeliveryStatuses() {
        DeliveryStatuses[] deliveryStatuses = orderService.getAllDeliveryStatuses();
        log.info("{}: Delivery status list has been retrieved", LogEnum.CONTROLLER);
        return deliveryStatuses;
    }

    @PutMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "update an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public OrderResponseDto update(@PathVariable String id, @Valid @RequestBody OrderRequestDto orderDto) {
        OrderResponseDto order = orderService.update(id, orderDto);
        log.info("{}: Order (id: {}) has been updated", LogEnum.CONTROLLER, order.id());
        return order;
    }

    @PatchMapping(URI_WITH_ID + "/delivery-status")
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "update the delivery status of an order. " +
            "All delivery statuses can be retrieved using the \"/api/orders/delivery-statuses\" (GET) endpoint. " +
            "Delivery statuses must be sent in sequence (e.g. PLACED -> PLACED_PROCESSED -> SENT, " +
            "it's forbidden to send PLACED -> SENT without the PLACED_PROCESSED step), " +
            "except for CANCELLED (which can only be used after the PLACED_PROCESSED status) " +
            "and FINISHED (which can be used after the DELIVERED and REFUNDED statuses)." +
            "A RETURN_REQUEST delivery status can be set using the \"/api/orders/{id}/return-request\" (POST) endpoint. " +
            "The next delivery statuses (RETURN_REQUEST_PROCESSED, RETURNED, etc.) are set using this (PATCH) endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the order delivery status",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public OrderResponseDto updateDeliveryStatus(@PathVariable String id, @RequestParam String deliveryStatus) {
        OrderResponseDto orderResponseDto = orderService.updateDeliveryStatus(id, deliveryStatus);
        log.info("{}: Order (id: {}) delivery status (on: {}) has been updated", LogEnum.CONTROLLER, id, deliveryStatus);
        return orderResponseDto;
    }

    @PostMapping(URI_WITH_ID + "/return-request")
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "submit a return request. Order must be delivered, but not finished.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted a return request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public OrderResponseDto submitReturnRequest(@PathVariable String id, @RequestParam String reason) {
        OrderResponseDto orderResponseDto = orderService.submitReturnRequest(id, reason);
        log.info("{}: Accepted a return request for an order (id: {}) ", LogEnum.CONTROLLER, id);
        return orderResponseDto;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "delete an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public void delete(@PathVariable String id) {
        orderService.delete(id);
        log.info("{}: Order (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
