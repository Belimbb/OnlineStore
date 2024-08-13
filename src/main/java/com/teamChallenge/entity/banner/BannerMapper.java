package com.teamChallenge.entity.banner;

import com.teamChallenge.dto.request.BannerRequestDto;
import com.teamChallenge.dto.response.BannerResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BannerMapper {

    public BannerResponseDto toResponseDto(BannerEntity entity){
        return new BannerResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCollectionName(),
                entity.getImageName());
    }

    public BannerEntity toEntity(BannerRequestDto dto){
        return new BannerEntity(
                dto.title(),
                dto.description(),
                dto.collectionName(),
                dto.imageName()
        );
    }

    public BannerEntity toEntity(BannerResponseDto dto){
        return new BannerEntity(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.collectionName(),
                dto.imageName()
        );
    }

    public List<BannerResponseDto> toResponseDtoList (Page<BannerEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<BannerResponseDto> toResponseDtoList (List<BannerEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<BannerEntity> toEntityListFromRequest (List<BannerRequestDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<BannerEntity> toEntityListFromResponse (List<BannerResponseDto> dtos){
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
