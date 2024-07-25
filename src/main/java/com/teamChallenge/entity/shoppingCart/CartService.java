package com.teamChallenge.entity.shoppingCart;

import java.util.List;

public interface CartService {

    List<CartDto> getAll();

    CartDto getById(String id);

    CartDto create(CartDto cartDto);

    CartDto update(String id, CartDto cartDto);

    boolean delete(String id);
}