package com.teamChallenge.entity.figure.sections.subCategory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategoryEntity, String> {
}
