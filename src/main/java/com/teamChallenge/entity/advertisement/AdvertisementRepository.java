package com.teamChallenge.entity.advertisement;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends MongoRepository<AdvertisementEntity, String>{
}
