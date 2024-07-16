package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.entity.figure.FigureEntity;

import java.util.List;

public record CartDto(String id, List<FigureEntity> figures, int price) {
}
