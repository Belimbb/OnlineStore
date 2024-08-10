package com.teamChallenge.dto.response;

import com.teamChallenge.entity.order.Statuses;
import com.teamChallenge.entity.user.address.AddressInfo;

import java.util.List;

public record OrderResponseDto(String id, AddressInfo address, int price, Statuses status,
                               List<FigureResponseDto> figureList, UserResponseDto userResponseDto) {
}
