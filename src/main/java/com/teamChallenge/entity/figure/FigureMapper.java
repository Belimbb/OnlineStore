package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class FigureMapper {
    private final SubCategoryMapper subCategoryMapper;

    public FigureResponseDto toResponseDto(FigureEntity entity){
        return new FigureResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getShortDescription(),
                entity.getLongDescription(),
                subCategoryMapper.toResponseDto(entity.getSubCategory()),
                entity.getLabel(),
                entity.getInWishList(),
                entity.getCurrentPrice(),
                entity.getOldPrice(),
                entity.getAmount(),
                entity.getColor(),
                entity.getImages()
                );
    }

    public FigureEntity toEntity (FigureRequestDto dto){
        return new FigureEntity(
                dto.name(),
                dto.shortDescription(),
                dto.longDescription(),
                null,
                dto.label(),
                false,
                dto.currentPrice(),
                dto.oldPrice(),
                dto.amount(),
                dto.color(),
                dto.images()
        );
    }

    public FigureEntity toEntity (FigureResponseDto dto){
        return new FigureEntity(
                dto.id(),
                dto.name(),
                dto.shortDescription(),
                dto.longDescription(),
                subCategoryMapper.toEntityFromResponse(dto.subCategory()),
                dto.label(),
                dto.inWishList(),
                dto.currentPrice(),
                dto.oldPrice(),
                dto.amount(),
                dto.color(),
                dto.images(),
                new Date()
        );
    }

    public List<FigureResponseDto> toResponseDtoList (List<FigureEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FigureEntity> toEntityListFromRequest (List<FigureRequestDto> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<FigureEntity> toEntityListFromResponse (List<FigureResponseDto> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
