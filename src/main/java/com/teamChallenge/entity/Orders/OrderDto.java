package com.teamChallenge.entity.Orders;

import com.teamChallenge.entity.Figures.FigureEntity;

import java.util.List;

public record OrderDto(
        String id,
        String address,
        int price,
        Statuses status,
        List<FigureEntity> figureList
) {
}
