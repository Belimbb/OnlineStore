package com.teamChallenge.controller;

import com.teamChallenge.dto.request.ReviewRequestDto;
import com.teamChallenge.dto.response.figure.FigureResponseDto;
import com.teamChallenge.dto.response.ReviewResponseDto;
import com.teamChallenge.entity.review.ReviewService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final static String SEC_REC = "BearerAuth";
    private static final String URI_WITH_ID = "/{id}";

    private final ReviewService reviewService;

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
    public ReviewResponseDto create(@Valid @RequestBody ReviewRequestDto review) {
        ReviewResponseDto reviewDto = reviewService.create(review);
        log.info("{}: Review (id: {}) has been added to Figure (id: {})", LogEnum.SERVICE, reviewDto.id(), review.figureId());
        return reviewDto;
    }

    @GetMapping
    @Operation(summary = "Get all reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reviews",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ReviewResponseDto.class)))}
            )
    })
    public List<ReviewResponseDto> getAll() {
        List<ReviewResponseDto> dtos = reviewService.getAll();

        log.info("{}: Reviews have been retrieved", LogEnum.CONTROLLER);
        return dtos;
    }

    @GetMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Get review by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review received"),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public ReviewResponseDto getById(@PathVariable String id) {
        ReviewResponseDto reviewResponseDto = reviewService.getById(id);
        log.info("{}: Review (id: {}) has been retrieved", LogEnum.SERVICE, id);
        return reviewResponseDto;
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
    public ReviewResponseDto update(@PathVariable String id, @Valid @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto updated = reviewService.update(id, requestDto);
        log.info("{}: Figure (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return updated;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted"),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id) {
        reviewService.delete(id);
        log.info("{}: Figure (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}