package com.teamChallenge.entity.Figures;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FigureRepository extends JpaRepository<FigureEntity, UUID> {
    Optional<List<FigureEntity>> findByCategory(Enum category);
    Optional<List<FigureEntity>> findByColor(String color);
}