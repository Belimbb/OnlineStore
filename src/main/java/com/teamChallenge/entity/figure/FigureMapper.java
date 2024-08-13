package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.figure.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryMapper;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryServiceImpl;
import com.teamChallenge.entity.review.ReviewMapper;
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

    private final ReviewMapper reviewMapper;

    public FigureResponseDto toResponseDto(FigureEntity entity){
        return new FigureResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getShortDescription(),
                entity.getLongDescription(),
                subCategoryMapper.toResponseDto(entity.getSubCategory()),
                entity.getLabel(),
                entity.getType(),
                entity.getCurrentPrice(),
                entity.getOldPrice(),
                entity.getAmount(),
                entity.getAdditionalInfo(),
                entity.getImages(),
                reviewMapper.toResponseDtoList(entity.getReviews()),
                entity.getAverageRating(),
                entity.getRatingDistribution()
                );
    }

    public FigureEntity toEntity (FigureRequestDto dto){
        return new FigureEntity(
                dto.name(),
                dto.shortDescription(),
                dto.longDescription(),
                subCategoryService.getByName(dto.subCategoryName()),
                dto.label(),
                dto.type(),
                dto.currentPrice(),
                dto.oldPrice(),
                dto.amount(),
                dto.images(),
                dto.additionalInfo()
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
                dto.type(),
                dto.currentPrice(),
                dto.oldPrice(),
                dto.amount(),
                dto.images(),
                dto.additionalInfo(),
                reviewMapper.toEntityListFromResponse(dto.reviewResponseDtoList()),
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
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<FigureEntity> toEntityListFromResponse (List<FigureResponseDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
