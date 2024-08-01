package com.teamChallenge.controller;

import com.teamChallenge.dto.request.CategoryRequestDto;
import com.teamChallenge.dto.response.CategoryResponseDto;
import com.teamChallenge.entity.figure.sections.category.CategoryService;
import com.teamChallenge.exception.CustomErrorResponse;
import com.teamChallenge.exception.LogEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private static final String URI_CATEGORY_WITH_ID = "/{id}";

    private final CategoryService categoryService;

    @GetMapping
    @Operation(description = "get all categories")
    @ApiResponse(responseCode = "200", description = "Received category List")
    public List<CategoryResponseDto> getAll() {
        List<CategoryResponseDto> categoryDtoList = categoryService.getAllCategories();
        log.info("{}: Category list has been retrieved", LogEnum.CONTROLLER);
        return categoryDtoList;
    }

    @GetMapping(URI_CATEGORY_WITH_ID)
    @Operation(description = "get a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public CategoryResponseDto getById(@PathVariable String id) {
        CategoryResponseDto category = categoryService.getById(id);
        log.info("{}: Category (id: {}) has been retrieved", LogEnum.CONTROLLER, id);
        return category;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "create a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the category",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
    })
    public CategoryResponseDto create(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto);
        log.info("{}: Category (id: {}) has been added", LogEnum.CONTROLLER, categoryResponseDto.id());
        return categoryResponseDto;
    }

    @PutMapping(URI_CATEGORY_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "update a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the category",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public CategoryResponseDto update(@PathVariable String id, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(id, categoryRequestDto);
        log.info("{}: Category (id: {}) has been updated", LogEnum.CONTROLLER, id);
        return categoryResponseDto;
    }

    @DeleteMapping(URI_CATEGORY_WITH_ID)
    @SecurityRequirement(name = "BearerAuth")
    @Operation(description = "delete a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the category",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CategoryResponseDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomErrorResponse.class))}
            )
    })
    public ResponseEntity<?> delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        log.info("{}: Category (id: {}) has been deleted", LogEnum.CONTROLLER, id);
        return ResponseEntity.ok().build();
    }
}
