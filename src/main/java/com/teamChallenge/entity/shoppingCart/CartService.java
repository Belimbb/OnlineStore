package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.request.CartRequestDto;
import com.teamChallenge.dto.response.CartResponseDto;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;

import java.util.List;

public interface CartService {

    List<CartResponseDto> getAll();

    CartResponseDto getById(String id);

    CartResponseDto create(UserEntity user, List<FigureEntity> figureList);

    CartResponseDto update(String id, CartRequestDto requestDto);

    void delete(String id);
}