package com.teamChallenge.controller;

import com.teamChallenge.entity.figure.FigureDto;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.SubCategory;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.request.FigureRequest;

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

    @GetMapping("/all")
    @Operation(summary = "Get all figures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureDto.class)))}
            )
    })
    public ResponseEntity<List<FigureDto>> figureList() throws CustomNotFoundException {
        List<FigureDto> figureDtos = figureService.getAllFigures();

        log.info("{}: Figures have been retrieved", LogEnum.CONTROLLER);
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
    public ResponseEntity<List<FigureDto>> figureListByCategory(@NotNull @Valid @RequestParam Category category) throws CustomNotFoundException {
        List<FigureDto> figureDtos = figureService.getAllFiguresByCategory(category);

        log.info("{}: Figures from category {} have been retrieved", LogEnum.CONTROLLER, category);
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
    public ResponseEntity<List<FigureDto>> figureListByCategory(@Valid @NotNull @RequestParam SubCategory subCategory) throws CustomNotFoundException {
        List<FigureDto> figureDtos = figureService.getAllFiguresBySubCategory(subCategory);

        log.info("{}: Figures from subCategory {} have been retrieved", LogEnum.CONTROLLER, subCategory);
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
    public ResponseEntity<FigureDto> getFigureById(@NotBlank @NotNull @PathVariable("figureId") String figureId) throws CustomNotFoundException{
        FigureDto figure = figureService.getById(figureId);
        log.info("{}: Figure (id: {}) has been retrieved", LogEnum.SERVICE, figure.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figure);
    }

    @PostMapping("/add")
    @Operation(summary = "Add new Figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Figure",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FigureDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<FigureDto> addFigure(@Valid @NotNull @RequestBody FigureRequest request) throws CustomAlreadyExistException {
        FigureDto figure = figureService.createFigure(request.name(), request.shortDescription(), request.longDescription(),
                request.subCategory(), request.price(), request.amount(), request.color(), request.images());

        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, figure.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(figure);
    }

    @DeleteMapping("/{figureId}")
    @Operation(summary = "Delete figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure deleted"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "BearerAuth")
    public void deleteUrlByShortId(@PathVariable("figureId") String  figureId) throws CustomNotFoundException {
        figureService.deleteFigure(figureId);

        log.info("{}: Figure (id: {}) has been deleted", LogEnum.CONTROLLER, figureId);
    }
}