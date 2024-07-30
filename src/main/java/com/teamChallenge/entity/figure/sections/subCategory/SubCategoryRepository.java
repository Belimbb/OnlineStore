package com.teamChallenge.entity.figure.sections.subCategory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategoryEntity, String> {
    Optional<SubCategoryEntity> findByName(String name);

    boolean existsByName(String name);
}
