package com.teamChallenge.entity.promoCode;

import com.teamChallenge.dto.response.PromoCodeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PromoCodeMapper {
    public PromoCodeResponseDto toResponseDto(PromoCodeEntity entity) {
        return new PromoCodeResponseDto(
                entity.getId(),
                entity.getCode(),
                entity.getDiscount(),
                entity.getExpirationDate()
        );
    }

    public PromoCodeEntity toEntity(PromoCodeResponseDto dto) {
        return new PromoCodeEntity(
                dto.id(),
                dto.code(),
                dto.discount(),
                dto.expirationDate()
        );
    }

    public List<PromoCodeResponseDto> toResponseDtoList(List<PromoCodeEntity> entities) {
        return entities == null ? null: entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<PromoCodeEntity> toEntityListFromResponse(List<PromoCodeResponseDto> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
