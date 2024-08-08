package com.teamChallenge.dto.request;

import com.teamChallenge.entity.user.address.AddressInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(@NotBlank @Size (min = 2, max = 50) String username,
                             @NotBlank @Size(min = 8, max = 100) String password,
                             AddressInfo addressInfo
) {
}
