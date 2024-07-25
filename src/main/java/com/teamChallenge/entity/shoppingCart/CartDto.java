package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;

import java.util.List;

public record CartDto(String id, List<FigureEntity> figures, UserEntity user, int price) {
}
