package com.teamChallenge.entity.ShoppingCart;

import com.teamChallenge.entity.Figures.FigureEntity;

import java.util.List;

public record CartDto(Long id, List<FigureEntity> figures, int price) {
}