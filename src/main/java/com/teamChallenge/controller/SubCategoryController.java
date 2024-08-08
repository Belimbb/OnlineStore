package com.teamChallenge.controller;

import com.teamChallenge.dto.request.SubCategoryRequestDto;
import com.teamChallenge.dto.response.SubCategoryResponseDto;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
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

    private final SubCategoryService subCategoryService;

    private static final String URI_SUBCATEGORIES_WITH_ID = "/{id}";
    private final String secReq = "BearerAuth";

    @GetMapping
    @Operation(description = "get all subCategories")
    @ApiResponse(responseCode = "200", description = "Received subCategory List")
    public List<SubCategoryResponseDto> getAll() {
        List<SubCategoryResponseDto> subCategoryList = subCategoryService.getAll();
        log.info("{}: SubCategory list has been retrieved", LogEnum.CONTROLLER);
        return subCategoryList;
    }

    @GetMapping(URI_SUBCATEGORIES_WITH_ID)
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "create a subCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the subCategory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubCategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    @SecurityRequirement(name = secReq)
    public SubCategoryResponseDto create(@Valid @RequestBody SubCategoryRequestDto subCategoryRequestDto) {
        SubCategoryResponseDto subCategoryResponseDto = subCategoryService.createSubCategory(subCategoryRequestDto);
        log.info("{}: SubCategory (id: {}) has been added", LogEnum.CONTROLLER, subCategoryResponseDto.id());
        return subCategoryResponseDto;
    }

    @PutMapping(URI_SUBCATEGORIES_WITH_ID)
    @Operation(description = "update a subCategory by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the subCategory",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubCategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "SubCategory not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    @SecurityRequirement(name = secReq)
    public SubCategoryResponseDto update(@Valid @RequestBody SubCategoryRequestDto subCategoryRequestDto, @PathVariable String id) {
        SubCategoryResponseDto subCategoryResponseDto = subCategoryService.updateSubCategory(id, subCategoryRequestDto);
        log.info("{}: SubCategory (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return subCategoryResponseDto;
    }

    @DeleteMapping(URI_SUBCATEGORIES_WITH_ID)
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
    @SecurityRequirement(name = secReq)
    public void delete(@PathVariable String id) {
        subCategoryService.deleteSubCategory(id);
        log.info("{}: SubCategory (id: {}) has been deleted", LogEnum.CONTROLLER, id);
    }

}
