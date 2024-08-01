package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.response.CartResponseDto;
import com.teamChallenge.entity.figure.FigureMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class CartMapper {

    private final FigureMapper figureMapper;

    public CartResponseDto toResponseDto(CartEntity entity){
        return new CartResponseDto(
                entity.getId(),
                figureMapper.toResponseDtoList(entity.getFigures()),
                entity.getPrice()
        );
    }

    public List<CartResponseDto> toDtoList (List<CartEntity> entities){
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
