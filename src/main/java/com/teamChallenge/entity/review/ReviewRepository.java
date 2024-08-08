package com.teamChallenge.entity.review;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
    Optional<List<ReviewEntity>> findByScore(byte score);

    boolean existsByUserAndFigure(UserEntity user, FigureEntity figure);
}
