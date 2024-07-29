package com.teamChallenge.controller;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;
import com.teamChallenge.entity.shoppingCart.CartService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private static final String URI_CART_WITH_ID = "/{id}";

    private final CartService cartService;

    @GetMapping("/all")
    @Operation(summary = "Get all ads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of carts",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CartResponseDto.class)))}
            )
    })
    public List<CartResponseDto> cartList() {
        List<CartResponseDto> cartList = cartService.getAll();
        log.info("{}: Cart list has been retrieved", LogEnum.CONTROLLER);
        return cartList;
    }

    @GetMapping(URI_CART_WITH_ID)
    @Operation(description = "get a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
                    ),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public CartResponseDto getById(@PathVariable String id) {
        CartResponseDto cart = cartService.getById(id);
        log.info("{}: Cart (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return cart;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "create a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the cart",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    public CartResponseDto create(@RequestBody CartRequestDto cartDto) {
        CartResponseDto cart = cartService.create(cartDto);
        log.info("{}: Cart (id: {}) has been added", LogEnum.CONTROLLER, cart.id());
        return cart;
    }

    @PutMapping(URI_CART_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "update a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the cart",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public CartResponseDto update(@PathVariable String id, @RequestBody CartRequestDto cartDto) {
        CartResponseDto cart = cartService.update(id, cartDto);
        log.info("{}: Cart (id: {}) has been updated", LogEnum.CONTROLLER, cart.id());
        return cart;
    }

    @DeleteMapping(URI_CART_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "delete a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the cart",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CartResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))
                    })
    })
    public void delete(@PathVariable String id) {
        cartService.delete(id);
        log.info("{}: Cart (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
