package com.teamChallenge.entity.figure.sections.category;

import com.teamChallenge.dto.request.CategoryRequestDto;
import com.teamChallenge.dto.response.CategoryResponseDto;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(String categoryName) throws CustomAlreadyExistException;
    CategoryResponseDto getById(String id) throws CustomNotFoundException;
    List<CategoryResponseDto> getAllCategories();
    CategoryResponseDto updateCategory(String id, CategoryRequestDto dto) throws CustomNotFoundException;
    void deleteCategory(String id) throws CustomNotFoundException;
}
