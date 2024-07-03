package com.teamChallenge.entity.Figures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FigureRepository extends JpaRepository<FigureEntity, Long> {
    Optional<FigureEntity> findById(UUID id);
    Optional<List<FigureEntity>> findByCategory(Enum category);
    Optional<List<FigureEntity>> findByColor(String color);

    boolean deleteById (UUID id);
    boolean existById (UUID id);
}