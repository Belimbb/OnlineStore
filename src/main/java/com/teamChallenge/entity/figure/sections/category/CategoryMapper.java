package com.teamChallenge.entity.figure.sections.category;

import com.teamChallenge.dto.request.CategoryRequestDto;
import com.teamChallenge.dto.response.CategoryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CategoryMapper {

    public CategoryResponseDto toResponseDto (CategoryEntity entity){
        return new CategoryResponseDto(
                entity.getId(),
                entity.getName()
        );
    }


    public CategoryEntity toEntityFromRequest(CategoryRequestDto dto){
        return new CategoryEntity(
                dto.name()
        );
    }

    public CategoryEntity toEntityFromResponse(CategoryResponseDto dto){
        return new CategoryEntity(
                dto.name(),
                dto.id()
        );
    }


    public List<CategoryResponseDto> toResponseDtoList(List<CategoryEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CategoryEntity> toEntityListFromRequest (List<CategoryRequestDto> dtos){
        return dtos.stream()
                .map(this::toEntityFromRequest)
                .collect(Collectors.toList());
    }

    public List<CategoryEntity> toEntityListFromResponse (List<CategoryResponseDto> dtos){
        return dtos.stream()
                .map(this::toEntityFromResponse)
                .collect(Collectors.toList());
    }
}