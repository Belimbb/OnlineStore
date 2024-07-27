package com.teamChallenge.entity.figure.sections.subCategory;

import com.teamChallenge.dto.request.SubCategoryRequestDto;
import com.teamChallenge.dto.response.SubCategoryResponseDto;
import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface SubCategoryService {
    SubCategoryResponseDto createSubCategory(String subCategoryName, CategoryEntity category) throws CustomAlreadyExistException;
    SubCategoryResponseDto getById(String id) throws CustomNotFoundException;
    List<SubCategoryResponseDto> getAll();
    SubCategoryResponseDto updateSubCategory(String id, SubCategoryRequestDto requestDto) throws CustomNotFoundException;
    void deleteSubCategory(String id) throws CustomNotFoundException;
}
