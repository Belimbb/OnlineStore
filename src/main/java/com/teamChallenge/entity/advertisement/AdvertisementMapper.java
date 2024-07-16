package com.teamChallenge.entity.advertisement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AdvertisementMapper {
    public AdvertisementDto toDto(AdvertisementEntity entity){
        return new AdvertisementDto(entity.getText(), entity.getUrl());
    }

    public AdvertisementEntity toEntity(AdvertisementDto dto){
        return new AdvertisementEntity(dto.text(), dto.url());
    }


    public List<AdvertisementDto> toDtoList(List<AdvertisementEntity> entities){
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AdvertisementEntity> toEntityList(List<AdvertisementDto> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
