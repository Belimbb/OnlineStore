package com.teamChallenge.controller;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.figure.FigureResponseDto;
import com.teamChallenge.entity.figure.FigureService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;

import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import com.teamChallenge.security.AccessValidator;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/figures")
public class FigureController {
    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";
    private static final String OBJECT_NAME = "figure";

    private final FigureService figureService;
    private final AccessValidator accessValidator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add new Figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Figure",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FigureResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public FigureResponseDto create(@Valid @RequestBody FigureRequestDto request) throws UnauthorizedAccessException {
        accessValidator.isAdmin();

        FigureResponseDto figure = figureService.create(request);

        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, figure.id());
        return figure;
    }

    @GetMapping
    @Operation(summary = "Get all figures. Now available those filters: \"features\", \"bestsellers\", \"in stock\", \"hot deals\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public List<FigureResponseDto> getAll(@RequestParam(required = false) String category, @RequestParam(required = false) String subcategory,
                                          @RequestParam(required = false) String filter, @RequestParam(required = false) String label,
                                          @RequestParam(required = false) String type, @RequestParam(required = false) String genre,
                                          @RequestParam(required = false) String brand, @RequestParam(required = false) String material,
                                          @RequestParam(required = false) String start_price, @RequestParam(required = false) String end_price,
                                          @RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String size) {

        List<FigureResponseDto> figureResponseDtos = figureService.getAll(category, subcategory, filter, label, type, genre,
                brand, material, start_price, end_price, page, size);
        log.info("{}: Figures have been retrieved", LogEnum.CONTROLLER);
        return figureResponseDtos;
    }

    @GetMapping("/ids")
    @Operation(summary = "Get all figures by array id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public List<FigureResponseDto> getAllByIdArray(@RequestParam String[] arrayId) {
        List<FigureResponseDto> figureResponseDtos = figureService.getAllByIdArray(arrayId);
        log.info("{}: Figures have been retrieved by array id", LogEnum.CONTROLLER);
        return figureResponseDtos;
    }

    @GetMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Get figure by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting figure",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = FigureResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    public FigureResponseDto getById(@PathVariable String id) {
        FigureResponseDto figure = figureService.getById(id);
        log.info("{}: Figure (id: {}) has been retrieved", LogEnum.SERVICE, figure.id());
        return figure;
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
    public FigureResponseDto update(@PathVariable String id, @Valid @RequestBody FigureRequestDto figureDto) throws UnauthorizedAccessException {
        accessValidator.isAdmin();

        FigureResponseDto figureResponseDto = figureService.update(id, figureDto);
        log.info("{}: Figure (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return figureResponseDto;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Delete figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure deleted"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void delete(@PathVariable String id) throws UnauthorizedAccessException {
        accessValidator.isAdmin();

        figureService.delete(id);
        log.info("{}: Figure (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }

    @PostMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(summary = "Add figure to wish list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure added"),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public void addFigureToWishList(@PathVariable String id) throws UnauthorizedAccessException {
        accessValidator.hasPermission(OBJECT_NAME, id);

        figureService.addFigureToWishList(id);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("{}: Figure (id: {}) has been added to User (email: {}) wish list", LogEnum.SERVICE, id, email);
    }
}