package com.teamChallenge.dto.request;

import com.teamChallenge.dto.request.figure.FigureInOrderRequestDto;
import com.teamChallenge.entity.user.address.AddressInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDto(@NotNull AddressInfo address,
                              @NotNull List<FigureInOrderRequestDto> figures) {
}
