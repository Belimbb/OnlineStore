package com.teamChallenge.controller;

import com.teamChallenge.entity.order.OrderDto;
import com.teamChallenge.entity.order.OrderService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private static final String URI_ORDER_WITH_ID = "/{id}";

    private final OrderService orderService;

    @GetMapping
    @Operation(description = "get all orders")
    @ApiResponse(responseCode = "200", description = "Received order List")
    public List<OrderDto> getAll() {
        List<OrderDto> orderList = orderService.getAll();
        log.info("{}: Order list has been retrieved", LogEnum.CONTROLLER);
        return orderList;
    }

    @GetMapping(URI_ORDER_WITH_ID)
    @Operation(description = "get an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public OrderDto getById(@PathVariable String id) {
        OrderDto order = orderService.getById(id);
        log.info("{}: Order (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return order;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    public OrderDto create(@RequestBody OrderDto orderDto) {
        OrderDto order = orderService.create(orderDto);
        log.info("{}: Order (id: {}) has been added", LogEnum.CONTROLLER, order.id());
        return order;
    }

    @PutMapping(URI_ORDER_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "update an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public OrderDto update(@PathVariable String id, @RequestBody OrderDto orderDto) {
        OrderDto order = orderService.update(id, orderDto);
        log.info("{}: Order (id: {}) has been updated", LogEnum.CONTROLLER, order.id());
        return order;
    }

    @DeleteMapping(URI_ORDER_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "delete an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public ResponseEntity delete(@PathVariable String id) {
        orderService.delete(id);
        log.info("{}: Order (id: {}) has been deleted", LogEnum.CONTROLLER, id);
        return ResponseEntity.ok().build();
    }
}
