package com.teamChallenge.controller;

import com.teamChallenge.dto.request.auth.LoginRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.user.auth.AuthService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.security.jwt.JwtResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "")
@RequestMapping("/auth")
public class AuthController {

    private static final String VERIF_URI = "/verification";
    private static final String VERIF_URI_CODE = "/{verifCode}";

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = JwtResponseDto.class)) }),
            @ApiResponse(responseCode = "4XX", description = "Login failed",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public JwtResponseDto authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) throws Exception {
        String jwtToken = authService.login(loginRequestDto);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("{}: User (email: {}) has accomplished authentication process", LogEnum.CONTROLLER, email);
        return new JwtResponseDto(jwtToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Registration successful",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponseDto.class)) }),
            @ApiResponse(responseCode = "4XX", description = "Registration failed",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public UserResponseDto registerUser(@Valid @RequestBody SignupRequestDto signUpRequestDto) throws CustomAlreadyExistException {
        UserResponseDto userDto = authService.signUp(signUpRequestDto);
        log.info("{}: User (id: {}) has accomplished registration process", LogEnum.CONTROLLER, userDto.id());
        return userDto;
    }

    @PostMapping(VERIF_URI+"/email")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Send email verif message")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Email verification message sent successfully"),
            @ApiResponse(responseCode = "404", description = "User with this email verification code not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public void sendEmailVerifMes(@Valid @RequestBody LoginRequestDto loginRequestDto) throws CustomAlreadyExistException {
        UserResponseDto userDto = authService.sentEmailVerifMes(loginRequestDto.getEmail());
        log.info("{}: Email verification email was sent to User (id: {})", LogEnum.CONTROLLER, userDto.id());
    }

    @GetMapping(VERIF_URI+"/email"+VERIF_URI_CODE)
    @Operation(summary = "User account verification")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Email verification successful"),
            @ApiResponse(responseCode = "404", description = "User with this email not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public void emailVerification(@PathVariable String verifCode) {
        UserResponseDto userResponseDto = authService.emailVerification(verifCode);
        log.info("{}: User (id: {}) has completed email verification", LogEnum.CONTROLLER, userResponseDto.id());
    }

    @PostMapping(VERIF_URI+"/password")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Send password verif message")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Password verification message sent successfully"),
            @ApiResponse(responseCode = "404", description = "User with this email not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public void sendPasswordVerifMes(@Valid @RequestBody LoginRequestDto loginRequestDto) throws CustomAlreadyExistException {
        UserResponseDto userDto = authService.sentPasswordVerifMes(loginRequestDto.getEmail());
        log.info("{}: Password verification email was sent to User (id: {})", LogEnum.CONTROLLER, userDto.id());
    }

    @GetMapping(VERIF_URI+"/password"+VERIF_URI_CODE)
    @Operation(summary = "User account verification")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Password verification successful"),
            @ApiResponse(responseCode = "404", description = "User with this password verification code not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public void passwordVerification(@PathVariable String verifCode) {
        UserResponseDto userResponseDto = authService.passwordVerification(verifCode);
        log.info("{}: User (id: {}) has completed password verification", LogEnum.CONTROLLER, userResponseDto.id());
    }

    @PostMapping(VERIF_URI+"/password/reset")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Reset password (update)")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Password reset"),
            @ApiResponse(responseCode = "404", description = "User with this email not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomErrorResponse.class)) })
    })
    public void resetPassword(@Valid @RequestBody LoginRequestDto loginRequestDto) throws CustomAlreadyExistException {
        UserResponseDto userDto = authService.resetPassword(loginRequestDto);
        log.info("{}: User (id: {}) password was reset", LogEnum.CONTROLLER, userDto.id());
    }
}