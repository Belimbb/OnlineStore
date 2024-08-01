package com.teamChallenge.entity.figure.sections.category;

import com.teamChallenge.dto.request.CategoryRequestDto;
import com.teamChallenge.dto.response.CategoryResponseDto;
import com.teamChallenge.entity.figure.FigureRepository;
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

    private final FigureRepository figureRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private static final String OBJECT_NAME = "Category";

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) throws CustomAlreadyExistException {
        String categoryName = categoryRequestDto.name();
        if (categoryRepository.existsByName(categoryName)){
            throw new CustomAlreadyExistException(OBJECT_NAME, categoryName);
        }

        CategoryEntity category = new CategoryEntity(categoryName);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto getById(String id) throws CustomNotFoundException {
        CategoryEntity category = findById(id);

        return categoryMapper.toResponseDto(category);
    }

    public CategoryEntity getByName(String categoryName) throws CustomNotFoundException {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, categoryName));
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
        CategoryEntity category = findById(id);
        figureRepository.deleteByCategory(category);

        categoryRepository.deleteById(id);
    }

    private CategoryEntity findById (String id){
        return categoryRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}
