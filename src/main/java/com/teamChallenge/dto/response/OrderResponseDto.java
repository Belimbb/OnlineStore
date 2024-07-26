package com.teamChallenge.dto.response;

import com.teamChallenge.entity.order.Statuses;

import java.util.List;

public record OrderResponseDto(String id, String address, int price, Statuses status, List<FigureResponseDto> figureList, UserResponseDto userResponseDto) {
}
