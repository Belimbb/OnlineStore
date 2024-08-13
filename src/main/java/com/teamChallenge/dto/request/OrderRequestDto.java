package com.teamChallenge.dto.request;

import com.teamChallenge.entity.order.Statuses;
import com.teamChallenge.entity.address.AddressInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(@NotNull Statuses status,
                              @NotNull AddressInfo address,
                              @NotBlank String cartId) {
}
