package com.teamChallenge.entity.Figures;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FigureRepository extends MongoRepository<FigureEntity, String> {
    Optional<List<FigureEntity>> findByCategory(String category);
    Optional<List<FigureEntity>> findBySubCategory(Enum<?> subCategory);
    Optional<List<FigureEntity>> findByColor(String color);
}