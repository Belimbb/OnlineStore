package com.teamChallenge.controller;

import com.teamChallenge.dto.request.UserRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.user.UserService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private static final String URI_USERS_WITH_ID = "/{id}";

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Add new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public UserResponseDto addUser(@Valid @RequestBody SignupRequestDto request) {
        UserResponseDto user = userService.create(request);
        log.info("{}: User (id: {}) has been added", LogEnum.SERVICE, user.id());
        return user;
    }

    @GetMapping
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List users",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))}
            )
    })
    public List<UserResponseDto> userList() {
        List<UserResponseDto> users = userService.getAll();
        log.info("{}: Users have been retrieved", LogEnum.CONTROLLER);
        return users;
    }

    @GetMapping(URI_USERS_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "get an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public UserResponseDto getById(@PathVariable String id) {
        UserResponseDto user = userService.getById(id);
        log.info("{}: User (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return user;
    }

    @PutMapping(URI_USERS_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "update an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public UserResponseDto update(@PathVariable String id, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = userService.update(id, userRequestDto);
        log.info("{}: User (id: {}) has been updated", LogEnum.CONTROLLER, user.id());
        return user;
    }

    @DeleteMapping(URI_USERS_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id) {
        userService.delete(id);
        log.info("{}: User (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }

    @DeleteMapping(URI_USERS_WITH_ID + "/wishlist")
    @Operation(summary = "Remove figure from wish list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure removed from wish list"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "BearerAuth")
    public void removeFigureFromWishList(@RequestBody String figureId) {
        userService.removeFigureFromWishList(figureId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("{}: Figure (id: {}) has been removed from User (email: {}) wish list", LogEnum.CONTROLLER, figureId, email);
    }
}