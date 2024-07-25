package com.teamChallenge.controller;

import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserServiceImpl userService;

    /*
    @PostMapping("/add")
    @Operation(summary = "Add new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Figure",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<FigureDto> addUser(@Valid @NotNull @RequestBody SignupRequestDto request, Principal principal) throws CustomAlreadyExistException, UnauthorizedAccessException {
        if (!userService.getByEmail(principal.getName()).role().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }
        UserResponseDto user = userService.create(request.getUsername(), request.getEmail(), request.getEmail());

        log.info("{}: User (id: {}) has been added", LogEnum.SERVICE, user.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
     */
    /*
    @GetMapping("/all")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List users",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))}
            )
    })
    public ResponseEntity<List<--->> userList() throws CustomNotFoundException {
        List<---> users = userService.getAll();

        log.info("{}: Users have been retrieved", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }
     */

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "BearerAuth")
    public void deleteUrlByShortId(@PathVariable("userId") String userId, Principal principal) throws CustomNotFoundException, UnauthorizedAccessException {
        if (!userService.getByEmail(principal.getName()).role().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }
        userService.delete(userId);

        log.info("{}: User (id: {}) has been deleted", LogEnum.CONTROLLER, userId);
    }
}
