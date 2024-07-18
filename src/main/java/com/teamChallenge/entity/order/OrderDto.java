package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureDto;
import com.teamChallenge.entity.figure.FigureEntity;

import java.util.List;

public record OrderDto(
        String id,
        String address,
        int price,
        Statuses status,
        List<FigureDto> figureList
) {
}
