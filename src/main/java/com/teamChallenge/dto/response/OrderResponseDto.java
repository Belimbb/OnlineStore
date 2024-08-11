package com.teamChallenge.dto.response;

import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.entity.order.Statuses;
import com.teamChallenge.entity.user.address.AddressInfo;

import java.util.Date;
import java.util.List;

public record OrderResponseDto(String id, AddressInfo address, int totalPrice, Statuses status,
                               List<FigureInCartOrderResponseDto> figureList, String userId, Date dateOfCompletion) {
}
