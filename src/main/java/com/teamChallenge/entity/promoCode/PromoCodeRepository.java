package com.teamChallenge.entity.promoCode;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodeRepository extends MongoRepository<PromoCodeEntity, String> {
    Optional<PromoCodeEntity> findByCode(String code);

    boolean existsByCode(String code);
}
