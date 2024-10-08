package com.teamChallenge.entity.user.auth;

import com.teamChallenge.dto.request.auth.LoginRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;

public interface AuthService {

    UserResponseDto signUp(SignupRequestDto signupRequestDto);

    String login(LoginRequestDto loginRequestDto) throws Exception;

    UserResponseDto emailVerification(String emailVerificationCode);

    UserResponseDto passwordVerification(String passwordVerificationCode);

    UserResponseDto sentEmailVerifMes(String email);

    UserResponseDto sentPasswordVerifMes(String email);

    UserResponseDto resetPassword(LoginRequestDto loginRequestDto);
}
