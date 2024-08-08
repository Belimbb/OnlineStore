package com.teamChallenge.controller;

import com.teamChallenge.dto.request.ReviewRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.dto.response.ReviewResponseDto;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.entity.user.review.ReviewServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;

import com.teamChallenge.security.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final static String SEC_REC = "BearerAuth";
    private static final String URI_WITH_ID = "/{id}";

    private final FigureServiceImpl figureService;
    private final UserServiceImpl userService;
    private final ReviewServiceImpl reviewService;
    private final AuthUtil authUtil;

    @PostMapping
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added Review",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FigureResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public ResponseEntity<FigureResponseDto> create(@RequestBody ReviewRequestDto review) throws CustomAlreadyExistException, UnauthorizedAccessException {
        ReviewResponseDto reviewDto = reviewService.create(review);
        FigureResponseDto figure = figureService.getById(review.figureId());

        log.info("{}: Review (id: {}) has been added to Figure (id: {})", LogEnum.SERVICE, reviewDto.id(), figure.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(figure);
    }

    @GetMapping
    @Operation(summary = "Get all reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reviews",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ReviewResponseDto.class)))}
            )
    })
    public ResponseEntity<List<ReviewResponseDto>> getAll() throws CustomNotFoundException {
        List<ReviewResponseDto> dtos = reviewService.getAll();

        log.info("{}: Reviews have been retrieved", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtos);
    }

    @PutMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Update figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure updated"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public ResponseEntity<ReviewResponseDto> update(@PathVariable String id,
                                                          @NotNull @RequestBody ReviewRequestDto requestDto,
                                                          Principal principal) throws CustomNotFoundException, UnauthorizedAccessException{
        validateIsOwnerOrAdmin(id, principal);

        ReviewResponseDto updated = reviewService.update(id, requestDto);
        log.info("{}: Figure (id: {}) has been updated", LogEnum.CONTROLLER, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updated);
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted"),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id, Principal principal) throws CustomNotFoundException, UnauthorizedAccessException {
        validateIsOwnerOrAdmin(id, principal);

        reviewService.delete(id);
        log.info("{}: Figure (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }



    private void validateIsOwnerOrAdmin(String reviewId, Principal principal) throws UnauthorizedAccessException {
        UserEntity user = userService.findByEmail(principal.getName());
        if(reviewService.findById(reviewId).getUser().equals(user) || authUtil.isAdmin(principal)){
            throw new UnauthorizedAccessException();
        }
    }
}