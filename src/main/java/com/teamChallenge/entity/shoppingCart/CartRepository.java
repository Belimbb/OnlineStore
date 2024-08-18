package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.entity.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<CartEntity, String> {
    Optional<List<CartEntity>> findByTotalPrice(int totalPrice);

    boolean existsByUser(UserEntity user);

    CartEntity findByUser(UserEntity user);
}
