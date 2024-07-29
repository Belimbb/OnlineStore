package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;

import java.util.List;

public interface CartService {

    List<CartResponseDto> getAll();

    CartResponseDto getById(String id);

    CartResponseDto create(CartRequestDto cartRequestDto);

    CartResponseDto update(String id, CartRequestDto requestDto);

    void delete(String id);
}