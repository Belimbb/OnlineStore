package com.teamChallenge.entity.advertisement;

import com.teamChallenge.dto.request.AdsRequestDto;
import com.teamChallenge.dto.response.AdsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AdvertisementMapper {
    public AdsResponseDto toResponseDto(AdvertisementEntity entity){
        return new AdsResponseDto(entity.getId(), entity.getText(), entity.getUrl());
    }

    public AdvertisementEntity toEntity(AdsRequestDto dto){
        return new AdvertisementEntity(dto.text(), dto.url());
    }


    public List<AdsResponseDto> toResponseDtoList(List<AdvertisementEntity> entities){
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<AdvertisementEntity> toEntityList(List<AdsRequestDto> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
