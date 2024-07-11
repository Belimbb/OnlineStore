package com.teamChallenge.entity.ShoppingCart;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<CartEntity, String> {
    Optional<List<CartEntity>> findByPrice(int price);
}
