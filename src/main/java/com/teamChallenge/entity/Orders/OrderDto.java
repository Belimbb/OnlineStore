package com.teamChallenge.entity.Orders;

import com.teamChallenge.entity.Products.Product;

import java.util.UUID;
import java.util.List;

public record OrderDto(
        UUID id,
        String address,
        int price,
        Statuses status,
        List<Product> products
) {
}
