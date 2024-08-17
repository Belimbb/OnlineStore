package com.teamChallenge.entity.user.auth;

import com.teamChallenge.dto.request.auth.LoginRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.exceptions.userExceptions.UnverifiedAccountException;
import com.teamChallenge.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public UserResponseDto signUp(SignupRequestDto signupRequestDto) {
        return userService.create(signupRequestDto);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) throws Exception {
        UserEntity user = userService.findByEmail(loginRequestDto.getEmail());

        if (!user.isEmailVerified()) {
            throw new UnverifiedAccountException(loginRequestDto.getEmail());
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Authentication Exception", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateToken(user);
    }

    @Override
    public UserResponseDto emailVerification(String emailVerificationCode) {
        return userService.confirmEmail(emailVerificationCode);
    }

    @Override
    public UserResponseDto passwordVerification(String passwordVerificationCode) {
        return userService.confirmPassword(passwordVerificationCode);
    }
}