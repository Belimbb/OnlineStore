package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FigureRepository extends MongoRepository<FigureEntity, String> {
    Optional<List<FigureEntity>> findByCategory(CategoryEntity category);
    Optional<List<FigureEntity>> findBySubCategory(SubCategoryEntity subCategory);
    Optional<List<FigureEntity>> findByColor(String color);

    boolean existsByUniqueHash (String uniqueHash);
}