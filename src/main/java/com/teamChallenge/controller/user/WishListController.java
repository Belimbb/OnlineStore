package com.teamChallenge.controller.user;

import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.FigureMapper;
import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/wishlist")
public class WishListController {

    private static final String URI_FIGURES_WITH_ID = "/{figureId}";

    private final UserServiceImpl userService;

    private final FigureMapper figureMapper;

    @GetMapping(URI_FIGURES_WITH_ID)
    @Operation(summary = "Get figure from wish list by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure got",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FigureResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<FigureResponseDto> getFigureFromWishListById(@NotBlank @NotNull @PathVariable("figureId") String figureId, Principal principal) throws CustomNotFoundException{
        UserEntity user = userService.findByEmail(principal.getName());
        FigureResponseDto figure = figureMapper.toResponseDto(userService.getFigureFromWishList(user, figureId));
        log.info("{}: Figure (id: {}) has been retrieved", LogEnum.SERVICE, figure.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figure);
    }

    @DeleteMapping(URI_FIGURES_WITH_ID)
    @Operation(summary = "Remove figure from wish list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure removed from wish list"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "BearerAuth")
    public void removeFigureFromWishList(@PathVariable String figureId, Principal principal) throws CustomNotFoundException, UnauthorizedAccessException {
        validation(principal);
        String email = principal.getName();

        userService.removeFigureFromWishList(email, figureId);

        log.info("{}: Figure (id: {}) has been removed from User (email: {}) wish list", LogEnum.CONTROLLER, figureId, email);
    }

    private void validation(Principal principal) throws UnauthorizedAccessException {
        if (!userService.findByEmail(principal.getName()).getRole().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }
    }
}
