package com.teamChallenge.controller;

import com.teamChallenge.dto.request.OrderRequestDto;
import com.teamChallenge.dto.response.OrderResponseDto;
import com.teamChallenge.entity.order.OrderService;
import com.teamChallenge.entity.order.OrderServiceImpl;
import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
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

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private static final String URI_ORDER_WITH_ID = "/{id}";

    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    @GetMapping
    @Operation(description = "get all orders")
    @ApiResponse(responseCode = "200", description = "Received order List")
    public List<OrderResponseDto> getAll() {
        List<OrderResponseDto> orderList = orderService.getAll();
        log.info("{}: Order list has been retrieved", LogEnum.CONTROLLER);
        return orderList;
    }

    @GetMapping(URI_ORDER_WITH_ID)
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "create an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    public OrderResponseDto create(@RequestBody OrderRequestDto orderDto, Principal principal) throws UnauthorizedAccessException {
        validation(principal);
        OrderResponseDto order = orderService.create(orderDto);
        log.info("{}: Order (id: {}) has been added", LogEnum.CONTROLLER, order.id());
        return order;
    }

    @PutMapping(URI_ORDER_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "update an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the order",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public OrderResponseDto update(@PathVariable String id, @RequestBody OrderRequestDto orderDto, Principal principal) throws UnauthorizedAccessException {
        validation(principal);
        OrderResponseDto order = orderService.update(id, orderDto);
        log.info("{}: Order (id: {}) has been updated", LogEnum.CONTROLLER, order.id());
        return order;
    }

    @DeleteMapping(URI_ORDER_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
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
    public ResponseEntity<?> delete(@PathVariable String id, Principal principal) throws UnauthorizedAccessException {
        validation(principal);
        orderService.delete(id);
        log.info("{}: Order (id: {}) has been deleted", LogEnum.CONTROLLER, id);
        return ResponseEntity.ok().build();
    }

    private void validation(Principal principal) throws UnauthorizedAccessException {
        if (!userService.findByEmail(principal.getName()).getRole().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }
    }
}
