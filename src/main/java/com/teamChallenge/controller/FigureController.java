package com.teamChallenge.controller;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.FigureService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/figures")
public class FigureController {

    private final FigureService figureService;

    private static final String URI_FIGURES_WITH_ID = "/{id}";
    private final String mediaType = "application/json";
    private final String secReq = "BearerAuth";

    @PostMapping
    @Operation(summary = "Add new Figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added new Figure",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = FigureResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = secReq)
    public FigureResponseDto addFigure(@Valid @RequestBody FigureRequestDto request) {
        FigureResponseDto figure = figureService.create(request);
        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, figure.id());
        return figure;
    }

    @GetMapping
    @Operation(summary = "Get all figures. Now available those filters: \"features\", \"bestsellers\", \"in stock\", \"hot deals\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = mediaType,
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public List<FigureResponseDto> figureList(@RequestParam(required = false) String filter, @RequestParam(required = false) String label,
                                                              @RequestParam(required = false) String category, @RequestParam(required = false) String subcategory,
                                                              @RequestParam(required = false) String start_price, @RequestParam(required = false) String end_price,
                                                              @RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String size) {
        List<FigureResponseDto> figureResponseDtos = figureService.getAll(category, subcategory, filter, label, start_price, end_price, page, size);

        log.info("{}: Figures have been retrieved", LogEnum.CONTROLLER);
        return figureResponseDtos;
    }

    @GetMapping(URI_FIGURES_WITH_ID)
    @Operation(summary = "Get figure by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting figure",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = FigureResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    @SecurityRequirement(name = secReq)
    public FigureResponseDto getFigureById(@PathVariable String id) {
        FigureResponseDto figure = figureService.getById(id);
        log.info("{}: Figure (id: {}) has been retrieved", LogEnum.SERVICE, figure.id());
        return figure;
    }

    @PutMapping(URI_FIGURES_WITH_ID)
    @Operation(summary = "Update figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure updated"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = secReq)
    public FigureResponseDto updateFigure(@PathVariable String id, @Valid @RequestBody FigureRequestDto figureDto) {
        FigureResponseDto figureResponseDto = figureService.update(id, figureDto);
        log.info("{}: Figure (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return figureResponseDto;
    }

    @DeleteMapping(URI_FIGURES_WITH_ID)
    @Operation(summary = "Delete figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure deleted"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @SecurityRequirement(name = "BearerAuth")
    public void deleteUrlByShortId(@PathVariable String id) {
        figureService.delete(id);
        log.info("{}: Figure (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }

    @PostMapping(URI_FIGURES_WITH_ID)
    @Operation(summary = "Add figure to wish list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure added"),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = mediaType,
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = "BearerAuth")
    public void addFigureToWishList(@PathVariable String id) {
        figureService.addFigureToWishList(id);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("{}: Figure (id: {}) has been added to User (email: {}) wish list", LogEnum.SERVICE, id, email);
    }
}