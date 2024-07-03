package com.teamChallenge.entity.ShoppingCart;

import java.util.List;
import java.util.UUID;

public record CartDto(Long id,List<UUID> productsIds, int price) {
}
