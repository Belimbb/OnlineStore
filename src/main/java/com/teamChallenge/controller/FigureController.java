package com.teamChallenge.controller;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.user.UserServiceImpl;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/api/figures")
public class FigureController {
    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";

    private final FigureServiceImpl figureService;
    private final UserServiceImpl userService;
    private final AuthUtil authUtil;

    @PostMapping()
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
    public ResponseEntity<FigureResponseDto> addFigure(@Valid @NotNull @RequestBody FigureRequestDto request, Principal principal) throws CustomAlreadyExistException, UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        FigureResponseDto figure = figureService.create(request);

        log.info("{}: Figure (id: {}) has been added", LogEnum.SERVICE, figure.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(figure);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all figures. Now available those filters: \"features\", \"bestsellers\", \"in stock\", \"hot deals\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public ResponseEntity<List<FigureResponseDto>> figureList(@RequestParam(required = false) String filter, @RequestParam(required = false) String label,
                                                              @RequestParam(required = false) String start_price, @RequestParam(required = false) String end_price,
                                                              @RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String size)
            throws CustomNotFoundException {
        List<FigureResponseDto> figureResponseDtos = figureService.getAll(filter, label, start_price, end_price, page, size);

        log.info("{}: Figures have been retrieved", LogEnum.CONTROLLER);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDtos);
    }


    @GetMapping("/all/by_category")
    @Operation(summary = "Get all figures by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures by category",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public ResponseEntity<List<FigureResponseDto>> figureListByCategory(@RequestParam String category) throws CustomNotFoundException {
        List<FigureResponseDto> figureResponseDtos = figureService.getAllFiguresByCategory(category);

        log.info("{}: Figures from category {} have been retrieved", LogEnum.CONTROLLER, category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDtos);
    }

    @GetMapping("/all/by_subcategory")
    @Operation(summary = "Get all figures by sub category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List figures by subCategory",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = FigureResponseDto.class)))}
            )
    })
    public ResponseEntity<List<FigureResponseDto>> figureListBySubCategory(@RequestParam String subCategory) throws CustomNotFoundException {
        List<FigureResponseDto> figureResponseDtos = figureService.getAllFiguresBySubCategory(subCategory);

        log.info("{}: Figures from subCategory {} have been retrieved", LogEnum.CONTROLLER, subCategory);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDtos);
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
    public ResponseEntity<FigureResponseDto> getFigureById(@NotBlank @NotNull @PathVariable String id) throws CustomNotFoundException{
        FigureResponseDto figure = figureService.getById(id);
        log.info("{}: Figure (id: {}) has been retrieved", LogEnum.SERVICE, figure.id());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figure);
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
    public ResponseEntity<FigureResponseDto> updateFigure(@PathVariable String id,
                                                          @NotNull @RequestBody FigureRequestDto figureDto,
                                                          Principal principal) throws CustomNotFoundException, UnauthorizedAccessException{
        authUtil.validateAdminRole(principal);

        FigureResponseDto figureResponseDto = figureService.update(id, figureDto);
        log.info("{}: Figure (id: {}) has been updated", LogEnum.CONTROLLER, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(figureResponseDto);
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete figure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Figure deleted"),
            @ApiResponse(responseCode = "404", description = "Figure not found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    public void deleteUrlByShortId(@PathVariable String id, Principal principal) throws CustomNotFoundException, UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);
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
    public void addFigureToWishList(@PathVariable String id, Principal principal) throws UnauthorizedAccessException {
        authUtil.validateAdminRole(principal);

        figureService.addFigureToUserWishList(principal.getName(), id);

        log.info("{}: Figure (id: {}) has been added to User (email: {}) wish list", LogEnum.SERVICE, id, principal.getName());
    }
}