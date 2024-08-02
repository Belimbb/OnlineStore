package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.category.CategoryServiceImpl;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryServiceImpl;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNullPointerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigureServiceImpl implements FigureService{

    private final FigureRepository figureRepository;

    private final FigureMapper figureMapper;

    private final SubCategoryServiceImpl subCategoryService;

    private final CategoryServiceImpl categoryService;

    private final UserServiceImpl userService;

    private static final String OBJECT_NAME = "Figure";

    @Override
    public FigureResponseDto createFigure(FigureRequestDto figureRequestDto) throws CustomAlreadyExistException {
        SubCategoryEntity subCategory = subCategoryService.getByName(figureRequestDto.subCategoryName());
        String name = figureRequestDto.name();
        FigureEntity figureEntity = new FigureEntity(name, figureRequestDto.shortDescription(), figureRequestDto.longDescription(),
                subCategory, figureRequestDto.label(),false, figureRequestDto.currentPrice(), figureRequestDto.oldPrice(),
                figureRequestDto.amount(), figureRequestDto.color(), figureRequestDto.images());

        if (figureRepository.existsByUniqueHash(figureEntity.getUniqueHash())){
            throw new CustomAlreadyExistException(OBJECT_NAME, name);
        }
        figureEntity.setCreatedAt(new Date());
        figureRepository.save(figureEntity);
        log.info("{}: " + OBJECT_NAME + " (Name: {}) was created", LogEnum.SERVICE, name);
        return figureMapper.toResponseDto(figureEntity);
    }

    @Override
    public FigureResponseDto getById(String id) {
        FigureEntity figure = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!email.equals("anonymousUser") && !email.isBlank()) {
            userService.addFigureToRecentlyViewedList(email, figure);
        }

        return figureMapper.toResponseDto(figure);
    }

    public void addFigureToUserWishList(String email, String figureId) {
        FigureEntity figure = findById(figureId);
        userService.addFigureToWishList(email, figure);
    }

    @Override
    public List<FigureResponseDto> getAllFigures(String filter, String labelName, String startPrice, String endPrice, String pageStr, String sizeStr) {
        int page = getIntegerFromString(pageStr);
        int size = getIntegerFromString(sizeStr);

        Pageable pageable = PageRequest.of(page, size);
        boolean isSortByPrice = (startPrice != null || endPrice != null);

        if (labelName != null) {
            if (isSortByPrice) {
                return figureMapper.toResponseDtoList(getFigureListByPriceRangeAndLabel(startPrice, endPrice, labelName, pageable));
            }

            return figureMapper.toResponseDtoList(getFigureListByLabelDESC(labelName, pageable));

        } else if (filter != null) {
            return figureMapper.toResponseDtoList(getFigureListByFilter(filter, pageable));

        } else if (isSortByPrice) {
            return figureMapper.toResponseDtoList(getFigureListByPriceRange(startPrice, endPrice, pageable));

        } else {
            Page<FigureEntity> figurePage = figureRepository.findAll(pageable);
            log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
            return figureMapper.toResponseDtoList(figurePage);
        }
    }

    public List<FigureResponseDto> getAllFiguresByCategory(String categoryName){
        CategoryEntity category = categoryService.getByName(categoryName);
        Optional<List<FigureEntity>> figureEntities = figureRepository.findByCategory(category);
        if (figureEntities.isPresent()){
            log.info("{}: All " + OBJECT_NAME + " by category {} retrieved from db", LogEnum.SERVICE, categoryName);
            return figureMapper.toResponseDtoList(figureEntities.get());
        }
        throw new CustomNotFoundException(OBJECT_NAME + "s");
    }

    public List<FigureResponseDto> getAllFiguresBySubCategory (String subCategoryName){
        SubCategoryEntity subCategory = subCategoryService.getByName(subCategoryName);
        Optional<List<FigureEntity>> figureEntities = figureRepository.findBySubCategory(subCategory);
        if (figureEntities.isPresent()){
            log.info("{}: All " + OBJECT_NAME + "s by sub category {} retrieved from db", LogEnum.SERVICE, subCategory);
            return figureMapper.toResponseDtoList(figureEntities.get());
        }
        throw new CustomNotFoundException(OBJECT_NAME);
    }

    @Override
    public FigureResponseDto updateFigure(String id, FigureRequestDto figure) {
        if (!figureRepository.existsById(id)){
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
        FigureEntity entity = figureMapper.toEntity(figure);
        entity.setId(id);

        FigureEntity savedFigure = figureRepository.save(entity);
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, savedFigure.getId());
        return figureMapper.toResponseDto(savedFigure);
    }

    @Override
    public void deleteFigure(String id) {
        FigureEntity figure = findById(id);
        figureRepository.delete(figure);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }

    public FigureEntity findById(String id) {
        return figureRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }

    public List<FigureEntity> getFigureListByFilter(String filter, Pageable pageable) {
        List<FigureEntity> figureList;

        switch (filter) {
            case "features":
                figureList = getFigureListByLabelsDESC(new Labels[]{Labels.EXCLUSIVE, Labels.LIMITED});
                break;
            case "bestsellers":
                figureList = getFiveBestSellers();
                break;

            default: throw new CustomNotFoundException("filter", filter);
        }

        log.info("{}: All " + OBJECT_NAME + "s (with filter: {}) retrieved from db", LogEnum.SERVICE, filter);
        return figureList;
    }

    public List<FigureEntity> getFigureListByLabelsDESC(Labels[] labels) {
        List<FigureEntity> figureList = new ArrayList<>();

        for (Labels label : labels) {
            List<FigureEntity> tempFigureList = figureRepository.findByLabel(label, Sort.Direction.DESC);
            figureList.addAll(tempFigureList.stream().toList());
        }

        return figureList
                .stream()
                .distinct()
                .toList();
    }

    public Page<FigureEntity> getFigureListByLabelDESC(String labelName, Pageable pageable) {
        Labels label = getLabelFromString(labelName);
        Page<FigureEntity> figurePage = figureRepository.findByLabel(label, Sort.Direction.DESC, pageable);
        log.info("{}: All " + OBJECT_NAME + "s (with label '" + labelName + "') retrieved from db", LogEnum.SERVICE);
        return figurePage;
    }

    public Labels getLabelFromString(String label) {
        try {
            return Labels.valueOf(label);
        }   catch (IllegalArgumentException ex) {
            throw new CustomNotFoundException("Label with 'name' " + label);
        }
    }

    public Integer getIntegerFromString(String strNumber) {
        try {
            return Integer.parseInt(strNumber);
        }   catch (NullPointerException | NumberFormatException ex) {
            throw new CustomNullPointerException(strNumber);
        }
    }

    public Page<FigureEntity> getFigureListByPriceRange(String startPriceStr, String endPriceStr, Pageable pageable) {
        if (startPriceStr != null && endPriceStr != null) {
            int startPrice = getIntegerFromString(startPriceStr);
            int endPrice = getIntegerFromString(endPriceStr);

            Page<FigureEntity> figurePage = figureRepository.findByCurrentPriceBetween(startPrice, endPrice, pageable);
            log.info("{}: All " + OBJECT_NAME + "s (with start price '" + startPrice
                    + "' and end price '" + endPrice + "') retrieved from db", LogEnum.SERVICE);
            return figurePage;
        }   else if (startPriceStr != null) {
            int startPrice = getIntegerFromString(startPriceStr);

            Page<FigureEntity> figurePage = figureRepository.findByCurrentPriceGreaterThan(startPrice, pageable);
            log.info("{}: All " + OBJECT_NAME + "s (with start price '" + startPrice + "') retrieved from db", LogEnum.SERVICE);
            return figurePage;
        }   else {
            int endPrice = getIntegerFromString(endPriceStr);

            Page<FigureEntity> figurePage = figureRepository.findByCurrentPriceLessThan(endPrice, pageable);
            log.info("{}: All " + OBJECT_NAME + "s (with end price '" + endPrice + "') retrieved from db", LogEnum.SERVICE);
            return figurePage;
        }
    }

    public Page<FigureEntity> getFigureListByPriceRangeAndLabel(String startPriceStr, String endPriceStr, String labelName, Pageable pageable) {
        Labels label = getLabelFromString(labelName);

        if (startPriceStr != null && endPriceStr != null) {
            int startPrice = getIntegerFromString(startPriceStr);
            int endPrice = getIntegerFromString(endPriceStr);

            Page<FigureEntity> figurePage = figureRepository.findByCurrentPriceBetweenAndLabel(startPrice, endPrice, label, pageable);
            log.info("{}: All " + OBJECT_NAME + "s (with label '" + labelName + "', with start price '" + startPrice
                    + "' and end price '" + endPrice + "') retrieved from db", LogEnum.SERVICE);
            return figurePage;
        }   else if (startPriceStr != null) {
            int startPrice = getIntegerFromString(startPriceStr);

            Page<FigureEntity> figurePage = figureRepository.findByCurrentPriceGreaterThanAndLabel(startPrice, label, pageable);
            log.info("{}: All " + OBJECT_NAME + "s (with label '" + labelName + "', with start price '" + startPrice
                    + ") retrieved from db", LogEnum.SERVICE);
            return figurePage;
        }   else {
            int endPrice = getIntegerFromString(endPriceStr);

            Page<FigureEntity> figurePage = figureRepository.findByCurrentPriceLessThanAndLabel(endPrice, label, pageable);
            log.info("{}: All " + OBJECT_NAME + "s (with label '" + labelName + "' and end price '" + endPrice
                    + "') retrieved from db", LogEnum.SERVICE);
            return figurePage;
        }
    }

    private List<FigureEntity> getFiveBestSellers(){
        Optional<List<FigureEntity>> bestSellers = figureRepository.findFiveBestSellers();
        if (bestSellers.isPresent()){
            log.info("{}: All " + OBJECT_NAME + "s that are best sellers retrieved from db", LogEnum.SERVICE);
            return bestSellers.get();
        }
        throw new CustomNotFoundException(OBJECT_NAME);
    }
}
