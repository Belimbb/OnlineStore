package com.teamChallenge.controller;

import com.teamChallenge.dto.request.UserRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.user.UserService;
import com.teamChallenge.entity.address.AddressInfo;
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

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";

    private final UserService userService;

    @PostMapping
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new User",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public UserResponseDto create(@Valid @RequestBody SignupRequestDto request) {
        UserResponseDto user = userService.create(request);
        log.info("{}: User (id: {}) has been added", LogEnum.SERVICE, user.id());
        return user;
    }

    @GetMapping()
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List users",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))}
            )
    })
    public List<UserResponseDto> getAll() {
        List<UserResponseDto> users = userService.getAll();
        log.info("{}: Users have been retrieved", LogEnum.CONTROLLER);
        return users;
    }

    @GetMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "get an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public UserResponseDto getById(@PathVariable String id) {
        UserResponseDto user = userService.getById(id);
        log.info("{}: User (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return user;
    }

    @PutMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "update an user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the user",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public UserResponseDto update(@PathVariable String id, @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto user = userService.update(id, userRequestDto);
        log.info("{}: User (id: {}) has been updated", LogEnum.CONTROLLER, user.id());
        return user;
    }

    @PatchMapping(URI_WITH_ID + "/addressInfo")
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "update an user's address info by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the user's address info",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public UserResponseDto updateAddressInfo(@PathVariable String id, @RequestBody AddressInfo addressInfo) {
        UserResponseDto user = userService.updateAddressInfo(id, addressInfo);
        log.info("{}: User's (id: {}) address info has been updated", LogEnum.CONTROLLER, user.id());
        return user;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id) {
        userService.delete(id);
        log.info("{}: User (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }

    @DeleteMapping(URI_WITH_ID + "/wishlist")
    @Operation(summary = "Remove figure from wish list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure removed from wish list"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void removeFigureFromWishList(@RequestBody String figureId) {
        userService.removeFigureFromWishList(figureId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("{}: Figure (id: {}) has been removed from User (email: {}) wish list", LogEnum.CONTROLLER, figureId, email);
    }
}
