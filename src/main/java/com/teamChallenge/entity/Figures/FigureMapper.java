package com.teamChallenge.entity.Figures;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FigureMapper {

    public FigureDto toDto (FigureEntity entity){
        return new FigureDto(
                entity.getId(),
                entity.getName(),
                entity.getShortDescription(),
                entity.getLongDescription(),
                entity.getCategory(),
                entity.getPrice(),
                entity.getAmount(),
                entity.getColor(),
                entity.getImages(),
                entity.getCreatedAt());
    }

    public FigureEntity toEntity (FigureDto dto){
        return new FigureEntity(
                dto.id(),
                dto.name(),
                dto.shortDescription(),
                dto.longDescription(),
                dto.category(),
                dto.price(),
                dto.amount(),
                dto.color(),
                dto.images(),
                dto.createdA());
    }

    public List<FigureDto> toDtoList (List<FigureEntity> entities){
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<FigureEntity> toEntityList (List<FigureDto> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
