package com.teamChallenge.entity.ShoppingCart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<List<CartEntity>> findByPrice(int price);
}
