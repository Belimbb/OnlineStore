package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;

import java.util.List;

public record OrderDto(
        String id,
        String address,
        int price,
        Statuses status,
        List<FigureEntity> figureList,
        UserEntity user
) {
}
