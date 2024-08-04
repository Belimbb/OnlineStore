package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryMapper;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class FigureMapper {
    private final SubCategoryMapper subCategoryMapper;
    private final SubCategoryServiceImpl subCategoryService;

    public FigureResponseDto toResponseDto(FigureEntity entity){
        return new FigureResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getShortDescription(),
                entity.getLongDescription(),
                subCategoryMapper.toResponseDto(entity.getSubCategory()),
                entity.getLabel(),
                entity.getCurrentPrice(),
                entity.getOldPrice(),
                entity.getAmount(),
                entity.getImages(),
                entity.getTheme(),
                entity.getMaterial(),
                entity.getCharacterName(),
                entity.getProductType(),
                entity.getTypeOfFigure(),
                entity.getCountry(),
                entity.getPackageSize(),
                entity.getToySize()
                );
    }

    public FigureEntity toEntity (FigureRequestDto dto){
        return new FigureEntity(
                dto.name(),
                dto.shortDescription(),
                dto.longDescription(),
                subCategoryService.getByName(dto.subCategoryName()),
                dto.label(),
                dto.currentPrice(),
                dto.oldPrice(),
                dto.amount(),
                dto.images(),
                dto.theme(),
                dto.material(),
                dto.characterName(),
                dto.productType(),
                dto.typeOfFigure(),
                dto.country(),
                dto.packageSize(),
                dto.toySize()
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
                dto.currentPrice(),
                dto.oldPrice(),
                dto.amount(),
                dto.images(),
                dto.theme(),
                dto.material(),
                dto.characterName(),
                dto.productType(),
                dto.typeOfFigure(),
                dto.country(),
                dto.packageSize(),
                dto.toySize(),
                new Date()
        );
    }

    public List<FigureResponseDto> toResponseDtoList (Page<FigureEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
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
