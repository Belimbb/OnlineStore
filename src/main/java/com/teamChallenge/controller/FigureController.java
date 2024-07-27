package com.teamChallenge.controller;

import com.teamChallenge.dto.request.CategoryRequestDto;
import com.teamChallenge.dto.request.SubCategoryRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.figure.sections.category.CategoryMapper;
import com.teamChallenge.entity.figure.sections.category.CategoryServiceImpl;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryMapper;
import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import com.teamChallenge.dto.request.FigureRequestDto;

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

import java.security.Principal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/figures")
public class FigureController {

    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;

    private final FigureServiceImpl figureService;
    private final UserServiceImpl userService;

    @GetMapping("/all")
    @Operation(summary = "Get all figures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public ResponseEntity<List<FigureResponseDto>> figureList() throws CustomNotFoundException {
        List<FigureResponseDto> figureResponseDtos = figureService.getAllFigures();

        log.info("{}: Figures have been retrieved", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDtos);
    }

    @GetMapping("/all/by_category")
    @Operation(summary = "Get all figures by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures by category",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public ResponseEntity<List<FigureResponseDto>> figureListByCategory(@NotNull @Valid @RequestParam CategoryRequestDto category) throws CustomNotFoundException {
        List<FigureResponseDto> figureResponseDtos = figureService.getAllFiguresByCategory(categoryMapper.toEntityFromRequest(category));

        log.info("{}: Figures from category {} have been retrieved", LogEnum.CONTROLLER, category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDtos);
    }

    @GetMapping("/all/by_subcategory")
    @Operation(summary = "Get all figures by sub category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures by subCategory",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public ResponseEntity<List<FigureResponseDto>> figureListBySubCategory(@Valid @NotNull @RequestParam SubCategoryRequestDto subCategory) throws CustomNotFoundException {
        List<FigureResponseDto> figureResponseDtos = figureService.getAllFiguresBySubCategory(subCategoryMapper.toEntityFromRequest(subCategory));

        log.info("{}: Figures from subCategory {} have been retrieved", LogEnum.CONTROLLER, subCategory);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDtos);
    }

    @GetMapping("/{figureId}")
    @Operation(summary = "Get figure by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Getting figure",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FigureResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    public ResponseEntity<FigureResponseDto> getFigureById(@NotBlank @NotNull @PathVariable("figureId") String figureId) throws CustomNotFoundException{
        FigureResponseDto figure = figureService.getById(figureId);
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
                            schema = @Schema(implementation = FigureResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<FigureResponseDto> addFigure(@Valid @NotNull @RequestBody FigureRequestDto request, Principal principal) throws CustomAlreadyExistException, UnauthorizedAccessException {
        //можно и так получать е-мейл, но работа через principal выглядит не так запутанно
        //String email = SecurityContextHolder.getContext().getAuthentication().getName();

        validation(principal);
        FigureResponseDto figure = figureService.createFigure(request.name(), request.shortDescription(), request.longDescription(),
                subCategoryMapper.toEntityFromRequest(request.subCategory()), request.label(), request.currentPrice(), request.oldPrice(), request.amount(), request.color(), request.images());

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
    public void deleteUrlByShortId(@PathVariable("figureId") String figureId, Principal principal) throws CustomNotFoundException, UnauthorizedAccessException {
        validation(principal);
        figureService.deleteFigure(figureId);

        log.info("{}: Figure (id: {}) has been deleted", LogEnum.CONTROLLER, figureId);
    }

    private void validation(Principal principal) throws UnauthorizedAccessException {
        if (!userService.findByEmail(principal.getName()).getRole().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }
    }
}