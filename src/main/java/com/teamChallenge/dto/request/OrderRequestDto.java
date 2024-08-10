package com.teamChallenge.dto.request;

import com.teamChallenge.dto.request.figure.FigureInCartOrderRequestDto;
import com.teamChallenge.entity.order.Statuses;
import com.teamChallenge.entity.user.address.AddressInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDto(@NotNull Statuses status,
                              @NotNull AddressInfo address,
                              @NotNull List<FigureInCartOrderRequestDto> figures) {
}
