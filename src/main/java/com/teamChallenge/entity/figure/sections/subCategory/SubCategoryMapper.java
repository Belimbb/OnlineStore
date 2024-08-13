package com.teamChallenge.entity.figure.sections.subCategory;

import com.teamChallenge.dto.request.SubCategoryRequestDto;
import com.teamChallenge.dto.response.SubCategoryResponseDto;
import com.teamChallenge.entity.figure.sections.category.CategoryMapper;
import com.teamChallenge.entity.figure.sections.category.CategoryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class SubCategoryMapper {
    private final CategoryMapper categoryMapper;
    public final CategoryServiceImpl categoryService;

    public SubCategoryResponseDto toResponseDto(SubCategoryEntity entity){
        return new SubCategoryResponseDto(
                entity.getId(),
                entity.getName(),
                categoryMapper.toResponseDto(entity.getCategory())
        );
    }

    public SubCategoryEntity toEntityFromRequest(SubCategoryRequestDto dto){
        return new SubCategoryEntity(
                dto.subCategoryName(),
                categoryService.getByName(dto.categoryName())
        );
    }

    public SubCategoryEntity toEntityFromResponse(SubCategoryResponseDto dto){
        return new SubCategoryEntity(
                dto.id(),
                dto.name(),
                categoryMapper.toEntityFromResponse(dto.category())
        );
    }


    public List<SubCategoryResponseDto> toResponseDtoList(List<SubCategoryEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<SubCategoryEntity> toEntityListFromRequest (List<SubCategoryRequestDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntityFromRequest)
                .collect(Collectors.toList());
    }

    public List<SubCategoryEntity> toEntityListFromResponse (List<SubCategoryResponseDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntityFromResponse)
                .collect(Collectors.toList());
    }
}
