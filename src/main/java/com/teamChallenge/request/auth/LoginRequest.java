package com.teamChallenge.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    @Email
    @Schema(example = "string@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}