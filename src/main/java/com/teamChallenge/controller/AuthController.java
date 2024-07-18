package com.teamChallenge.controller;

import com.teamChallenge.entity.user.*;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.userExceptions.UserAlreadyExistException;
import com.teamChallenge.request.auth.LoginRequest;
import com.teamChallenge.request.auth.SignupRequest;
import com.teamChallenge.security.jwt.JwtResponseDto;
import com.teamChallenge.security.jwt.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "")
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDto.class)) }),
            @ApiResponse(responseCode = "4XX", description = "Login failed",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Authentication Exception", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = userMapper.toEntity(userService.getByEmail(loginRequest.getEmail()));
        String jwt = jwtUtils.generateToken(user);

        log.info("{}: User (id: {}) has accomplished authentication process", LogEnum.CONTROLLER, user.getId());
        return ResponseEntity.ok(new JwtResponseDto(jwt));
    }


    @PostMapping("/register")
    @Operation(summary = "Register user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Registration successful",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "4XX", description = "Registration failed",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws UserAlreadyExistException {
        UserDto userDto = userService.create(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());
        log.info("{}: User (id: {}) has accomplished registration process", LogEnum.CONTROLLER, userDto.id());
        return ResponseEntity.status(201).body(userDto);
    }
}