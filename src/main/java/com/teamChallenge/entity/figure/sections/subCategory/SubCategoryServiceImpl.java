package com.teamChallenge.entity.figure.sections.subCategory;

import com.teamChallenge.dto.request.SubCategoryRequestDto;
import com.teamChallenge.dto.response.SubCategoryResponseDto;
import com.teamChallenge.entity.figure.FigureRepository;
import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.category.CategoryServiceImpl;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final FigureRepository figureRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategoryMapper subCategoryMapper;

    private final CategoryServiceImpl categoryService;
    private static final String OBJECT_NAME = "SubCategory";

    @Override
    public SubCategoryResponseDto createSubCategory(SubCategoryRequestDto subCategoryRequestDto) throws CustomAlreadyExistException {
        String subCategoryName = subCategoryRequestDto.subCategoryName();
        CategoryEntity category = categoryService.getByName(subCategoryRequestDto.categoryName());

        if (subCategoryRepository.existsByName(subCategoryName)){
            throw new CustomAlreadyExistException(OBJECT_NAME, subCategoryName);
        }
        return subCategoryMapper.toResponseDto(subCategoryRepository.save(new SubCategoryEntity(subCategoryName, category)));
    }

    @Override
    public SubCategoryResponseDto getById(String id) throws CustomNotFoundException {
        SubCategoryEntity entity = findById(id);
        return subCategoryMapper.toResponseDto(entity);
    }

    @Override
    public List<SubCategoryResponseDto> getAll() {
        return subCategoryMapper.toResponseDtoList(subCategoryRepository.findAll());
    }

    @Override
    public SubCategoryResponseDto updateSubCategory(String id, SubCategoryRequestDto requestDto) throws CustomNotFoundException {
        if (!subCategoryRepository.existsById(id)){
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
        SubCategoryEntity entity = subCategoryMapper.toEntityFromRequest(requestDto);
        entity.setId(id);
        return subCategoryMapper.toResponseDto(entity);
    }

    @Override
    public void deleteSubCategory(String id) throws CustomNotFoundException {
        SubCategoryEntity subCategory = findById(id);
        figureRepository.deleteBySubCategory(subCategory);

        subCategoryRepository.deleteById(id);
    }

    public SubCategoryEntity getByName(String subCategoryName) {
        return subCategoryRepository.findByName(subCategoryName).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, subCategoryName));
    }

    private SubCategoryEntity findById(String id){
        return subCategoryRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}
