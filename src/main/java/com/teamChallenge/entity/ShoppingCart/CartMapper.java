package com.teamChallenge.entity.ShoppingCart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CartMapper {

    public CartEntity toEntity (CartDto dto){
        return new CartEntity(
                dto.id(),
                dto.figures(),
                dto.price()
        );
    }

    public CartDto toDto (CartEntity entity){
        return new CartDto(
                entity.getId(),
                entity.getFigures(),
                entity.getPrice()
        );
    }

    public List<CartEntity> toEntityList (List<CartDto> dtos){
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<CartDto> toDtoList (List<CartEntity> entities){
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
