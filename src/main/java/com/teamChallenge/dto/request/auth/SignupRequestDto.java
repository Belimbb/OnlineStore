package com.teamChallenge.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Size (min = 2, max = 50)
    private String username;

    @NotBlank
    @Email
    @Schema(example = "string@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}