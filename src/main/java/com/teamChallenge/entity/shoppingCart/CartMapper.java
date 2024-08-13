package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.response.CartResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class CartMapper {

    public CartResponseDto toResponseDto(CartEntity entity){
        return new CartResponseDto(
                entity.getId(),
                entity.getFigures(),
                entity.getTotalPrice()
        );
    }

    public List<CartResponseDto> toDtoList (List<CartEntity> entities){
        return entities == null ? null : entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
