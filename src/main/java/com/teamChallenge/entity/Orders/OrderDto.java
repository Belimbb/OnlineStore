package com.teamChallenge.entity.Orders;

import com.teamChallenge.entity.Figures.FigureEntity;

import java.util.List;
import java.util.UUID;

public record OrderDto(
        UUID id,
        String address,
        int price,
        Statuses status,
        List<FigureEntity> figureList
) {
}
