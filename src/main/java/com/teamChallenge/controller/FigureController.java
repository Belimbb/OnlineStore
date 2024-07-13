package com.teamChallenge.controller;

import com.teamChallenge.entity.Figures.FigureDto;
import com.teamChallenge.entity.Figures.FigureMapper;
import com.teamChallenge.entity.Figures.FigureServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.figureExceptions.FigureNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/figures")
public class FigureController {

    private final FigureServiceImpl figureService;
    private final FigureMapper figureMapper;

    @GetMapping("/all")
    @Operation(summary = "Get all figures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureDto.class)))}
            )
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<FigureDto>> figureList() throws FigureNotFoundException {
        List<FigureDto> figureDtos = figureService.getAllFigures();

        log.info("{}: Figures were retrieved from the database", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureDtos);
    }

    @GetMapping("/all/by_category")
    @Operation(summary = "Get all figures by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures by category",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureDto.class)))}
            )
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<FigureDto>> figureListByCategory(@NotNull @Valid @RequestParam String category) throws FigureNotFoundException {
        List<FigureDto> figureDtos = figureService.getAllFiguresByCategory(category);

        log.info("{}: Figures from category {} were retrieved from the database", LogEnum.CONTROLLER, category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureDtos);
    }

    @GetMapping("/all/by_subcategory")
    @Operation(summary = "Get all figures by sub category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures by subCategory",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureDto.class)))}
            )
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<List<FigureDto>> figureListByCategory(@Valid @NotNull @RequestParam Enum<?> subCategory) throws FigureNotFoundException {
        List<FigureDto> figureDtos = figureService.getAllFiguresBySubCategory(subCategory);

        log.info("{}: Figures from subCategory {} were retrieved from the database", LogEnum.CONTROLLER, subCategory);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureDtos);
    }

    @GetMapping("/{figureId}")
    @Operation(summary = "Get figure by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting figure",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FigureDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<FigureDto> getFigureById(@NotBlank @NotNull @PathVariable("figureId") String figureId) throws FigureNotFoundException{
        FigureDto figure = figureService.getById(figureId);
        log.info("{}: Figure (id: {}) was retrieved from the database", LogEnum.SERVICE, figure.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figure);
    }
}