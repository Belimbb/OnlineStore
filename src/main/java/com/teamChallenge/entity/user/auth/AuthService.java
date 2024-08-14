package com.teamChallenge.entity.user.auth;

import com.teamChallenge.dto.request.auth.LoginRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;

public interface AuthService {

    UserResponseDto signUp(SignupRequestDto signupRequestDto);

    String login(LoginRequestDto loginRequestDto) throws Exception;

    UserResponseDto verification(String verificationCode);
}
