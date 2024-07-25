package com.teamChallenge.entity.user;

import com.teamChallenge.dto.response.FigureResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public record UserDto(
        String id,
        @NotBlank @Email String email,
        @NotBlank String username,
        @NotBlank @Size(min = 3, max = 100) String password,
        @NotNull Roles role,
        Date createdAt,
        List<FigureResponseDto> wishList
) {}