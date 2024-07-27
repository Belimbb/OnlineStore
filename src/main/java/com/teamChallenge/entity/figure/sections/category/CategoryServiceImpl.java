package com.teamChallenge.entity.figure.sections.category;

import com.teamChallenge.dto.request.CategoryRequestDto;
import com.teamChallenge.dto.response.CategoryResponseDto;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private static final String OBJECT_NAME = "Category";

    @Override
    public CategoryResponseDto createCategory(String categoryName) throws CustomAlreadyExistException {
        if (categoryRepository.existsByName(categoryName)){
            throw new CustomAlreadyExistException(OBJECT_NAME, categoryName);
        }

        CategoryEntity category = new CategoryEntity(categoryName);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto getById(String id) throws CustomNotFoundException {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));

        return categoryMapper.toResponseDto(category);
    }

    public CategoryResponseDto getByName(String categoryName) throws CustomNotFoundException{
        CategoryEntity category = categoryRepository.findByName(categoryName).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, categoryName));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryMapper.toResponseDtoList(categoryRepository.findAll());
    }

    @Override
    public CategoryResponseDto updateCategory(String id, CategoryRequestDto dto) throws CustomNotFoundException {
        if (!categoryRepository.existsById(id)){
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
        CategoryEntity entity = categoryMapper.toEntityFromRequest(dto);
        entity.setId(id);

        return categoryMapper.toResponseDto(categoryRepository.save(entity));
    }

    @Override
    public void deleteCategory(String id) throws CustomNotFoundException {
        if (!categoryRepository.existsById(id)){
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }

        categoryRepository.deleteById(id);
    }
}
