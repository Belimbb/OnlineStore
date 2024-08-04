package com.teamChallenge.entity.user.review;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
    Optional<List<ReviewEntity>> findByScore(byte score);
}
