package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FigureRepository extends MongoRepository<FigureEntity, String> {
    Optional<List<FigureEntity>> findByCategory(CategoryEntity category);
    Optional<List<FigureEntity>> findBySubCategory(SubCategoryEntity subCategory);
    Optional<List<FigureEntity>> findByAmountGreaterThan(int amount);

    List<FigureEntity> findByLabel(Labels label, Sort.Direction direction);

    void deleteByCategory (CategoryEntity category);
    void deleteBySubCategory (SubCategoryEntity subCategory);

    @Aggregation(pipeline = {
            "{ '$sort': { 'purchaseCount': -1 } }",
            "{ '$limit': 5 }"
    })
    Optional<List<FigureEntity>> findFiveBestSellers();

    boolean existsByUniqueHash (String uniqueHash);
}