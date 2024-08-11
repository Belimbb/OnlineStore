package com.teamChallenge.entity.promoCode;

import com.teamChallenge.dto.response.PromoCodeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return entities
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<PromoCodeEntity> toEntityListFromResponse(List<PromoCodeResponseDto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }
}
