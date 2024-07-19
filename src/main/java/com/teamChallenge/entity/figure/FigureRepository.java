package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FigureRepository extends MongoRepository<FigureEntity, String> {
    Optional<List<FigureEntity>> findByCategory(Category category);
    Optional<List<FigureEntity>> findBySubCategory(SubCategory subCategory);
    Optional<List<FigureEntity>> findByColor(String color);

    boolean existsByUniqueHash (String uniqueHash);
}