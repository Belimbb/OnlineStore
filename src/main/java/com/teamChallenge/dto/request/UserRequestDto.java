package com.teamChallenge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(@NotBlank @Size (min = 2, max = 50) String username,
                             @NotBlank @Email String email,
                             @NotBlank @Size (min = 8, max = 100) String password,
                             String phoneNumber
) {
}
