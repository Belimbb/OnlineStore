package com.teamChallenge.controller;

import com.teamChallenge.dto.request.SubCategoryRequestDto;
import com.teamChallenge.dto.response.SubCategoryResponseDto;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryService;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subCategories")
@RequiredArgsConstructor
@Slf4j
public class SubCategoryController {

    private static final String URI_WITH_ID = "/{id}";
    private static final String SEC_REC = "BearerAuth";

    private final SubCategoryService subCategoryService;
    private final AccessValidator accessValidator;

    @PostMapping
    @SecurityRequirement(name = SEC_REC)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "create a subCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the subCategory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubCategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    public SubCategoryResponseDto create(@Valid @RequestBody SubCategoryRequestDto subCategoryRequestDto) throws UnauthorizedAccessException {
        accessValidator.isAdmin();

        SubCategoryResponseDto subCategoryResponseDto = subCategoryService.createSubCategory(subCategoryRequestDto);
        log.info("{}: SubCategory (id: {}) has been added", LogEnum.CONTROLLER, subCategoryResponseDto.id());
        return subCategoryResponseDto;
    }

    @GetMapping
    @Operation(description = "get all subCategories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of sub categories",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SubCategoryResponseDto.class)))}
            )
    })
    public List<SubCategoryResponseDto> getAll() {
        List<SubCategoryResponseDto> subCategoryList = subCategoryService.getAll();
        log.info("{}: SubCategory list has been retrieved", LogEnum.CONTROLLER);
        return subCategoryList;
    }

    @GetMapping(URI_WITH_ID)
    @Operation(description = "get a subCategory by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the subCategory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubCategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "SubCategory not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public SubCategoryResponseDto getById(@PathVariable String id) {
        SubCategoryResponseDto subCategory = subCategoryService.getById(id);
        log.info("{}: SubCategory (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return subCategory;
    }

    @PutMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "update a subCategory by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the subCategory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubCategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "SubCategory not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public SubCategoryResponseDto update(@Valid @RequestBody SubCategoryRequestDto subCategoryRequestDto, @PathVariable String id) throws UnauthorizedAccessException {
        accessValidator.isAdmin();

        SubCategoryResponseDto subCategoryResponseDto = subCategoryService.updateSubCategory(id, subCategoryRequestDto);
        log.info("{}: SubCategory (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return subCategoryResponseDto;
    }

    @DeleteMapping(URI_WITH_ID)
    @SecurityRequirement(name = SEC_REC)
    @Operation(description = "delete a subCategory by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the subCategory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubCategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "SubCategory not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public void delete(@PathVariable String id) throws UnauthorizedAccessException {
        accessValidator.isAdmin();

        subCategoryService.deleteSubCategory(id);
        log.info("{}: SubCategory (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }
}
