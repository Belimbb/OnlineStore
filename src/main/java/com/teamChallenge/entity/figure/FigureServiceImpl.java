package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureInCartRequestDto;
import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.dto.response.figure.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.Types;
import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.category.CategoryServiceImpl;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryServiceImpl;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.entity.review.ReviewEntity;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomBadRequestException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNullPointerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigureServiceImpl implements FigureService {
    private static final String OBJECT_NAME = "Figure";

    private final FigureRepository figureRepository;

    private final FigureMapper figureMapper;

    private final SubCategoryServiceImpl subCategoryService;
    private final CategoryServiceImpl categoryService;
    private final UserServiceImpl userService;


    @Override
    public FigureResponseDto create(FigureRequestDto figureRequestDto) throws CustomAlreadyExistException {
        String name = figureRequestDto.name();
        FigureEntity figureEntity = figureMapper.toEntity(figureRequestDto);

        if (figureRepository.existsByUniqueHash(figureEntity.getUniqueHash())) {
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

    @Override
    public void addFigureToWishList(String figureId) {
        FigureEntity figure = findById(figureId);
        userService.addFigureToWishList(figure);
    }

    @Override
    public List<FigureResponseDto> getAll(String categoryName, String subcategoryName, String filter, String labelName, String type, String genre,
                                          String brand, String material, String startPrice, String endPrice, String pageStr, String sizeStr) {
        List<FigureEntity> figureList;

        if (categoryName != null) {
            figureList = getFigureListByCategory(categoryName);

        } else if (subcategoryName != null) {
            figureList = getFigureListBySubCategory(subcategoryName);

        } else if (labelName != null) {
            figureList = getFigureListByLabelDESC(labelName);

        } else if (filter != null) {
            figureList = getFigureListByFilter(filter);

        } else {
            figureList = figureRepository.findAll();
            log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        }

        List<FigureEntity> sortedFigureList = sortByFields(figureList, type, genre, brand, material, startPrice, endPrice);
        Pageable pageable = getPageable(getIntegerFromString(pageStr), getIntegerFromString(sizeStr));
        Page<FigureEntity> figurePage = paginateFigureList(sortedFigureList, pageable);
        return figureMapper.toResponseDtoList(figurePage);
    }

    @Override
    public FigureResponseDto update(String id, FigureRequestDto figure) {
        if (!figureRepository.existsById(id)) {
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
        FigureEntity entity = figureMapper.toEntity(figure);
        entity.setId(id);

        FigureEntity savedFigure = figureRepository.save(entity);
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, savedFigure.getId());
        return figureMapper.toResponseDto(savedFigure);
    }

    @Override
    public void delete(String id) {
        FigureEntity figure = findById(id);
        figureRepository.delete(figure);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }


    public FigureEntity findById(String id) {
        return figureRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }

    public boolean existById(String id) {
        return figureRepository.existsById(id);
    }

    //FILTER
    private List<FigureEntity> getFigureListByFilter(String filter) {
        List<FigureEntity> figureList = switch (filter) {
            case "features" -> getFigureListByLabelsDESC(new Labels[]{Labels.EXCLUSIVE, Labels.LIMITED});
            case "bestsellers" -> getFiveBestSellers();
            case "in stock" -> getInStockOnly();
            case "hot deals" -> getFigureListByLabelDESC(Labels.SALE.name());
            default -> throw new CustomNotFoundException("filter", filter);
        };

        log.info("{}: All " + OBJECT_NAME + "s (with filter: {}) retrieved from db", LogEnum.SERVICE, filter);
        return figureList;
    }

    private List<FigureEntity> getInStockOnly() {
        Optional<List<FigureEntity>> figures = figureRepository.findByAmountGreaterThan(0);
        if (figures.isPresent()) {
            log.info("{}: All " + OBJECT_NAME + "s that are in stock retrieved from db", LogEnum.SERVICE);
            return figures.get();
        }
        throw new CustomNotFoundException(OBJECT_NAME);
    }

    private List<FigureEntity> getFiveBestSellers() {
        Optional<List<FigureEntity>> bestSellers = figureRepository.findFiveBestSellers();
        if (bestSellers.isPresent()) {
            log.info("{}: All " + OBJECT_NAME + "s that are best sellers retrieved from db", LogEnum.SERVICE);
            return bestSellers.get();
        }
        throw new CustomNotFoundException(OBJECT_NAME);
    }


    //FIGURE LIST BY SECTION
    private List<FigureEntity> getFigureListByCategory(String categoryName) {
        CategoryEntity category = categoryService.getByName(categoryName);
        List<FigureEntity> figureEntities = figureRepository.findByCategory(category);
        log.info("{}: All " + OBJECT_NAME + " by category {} retrieved from db", LogEnum.SERVICE, categoryName);
        return figureEntities;
    }

    private List<FigureEntity> getFigureListBySubCategory(String subCategoryName) {
        SubCategoryEntity subCategory = subCategoryService.getByName(subCategoryName);
        List<FigureEntity> figureEntities = figureRepository.findBySubCategory(subCategory);
        log.info("{}: All " + OBJECT_NAME + "s by sub category {} retrieved from db", LogEnum.SERVICE, subCategory);
        return figureEntities;
    }


    //FIGURE LIST BY LABEL(-s)
    private List<FigureEntity> getFigureListByLabelsDESC(Labels[] labels) {
        List<FigureEntity> figureList = new ArrayList<>();

        for (Labels label : labels) {
            List<FigureEntity> tempFigureList = figureRepository.findByLabel(label, Sort.Direction.DESC);
            figureList.addAll(tempFigureList.stream().toList());
        }

        return figureList;
    }

    private List<FigureEntity> getFigureListByLabelDESC(String labelName) {
        Labels label = getLabelFromString(labelName);
        List<FigureEntity> figurePage = figureRepository.findByLabel(label, Sort.Direction.DESC);
        log.info("{}: All " + OBJECT_NAME + "s (with label '{}') retrieved from db", LogEnum.SERVICE, labelName);
        return figurePage;
    }


    //MAP STRING TO OBJECT
    private Labels getLabelFromString(String labelName) {
        try {
            return Labels.valueOf(labelName);
        } catch (IllegalArgumentException ex) {
            throw new CustomNotFoundException("Label with name '" + labelName + "' ");
        }
    }

    public Types getTypeFromString(String typeName) {
        try {
            return Types.valueOf(typeName);
        } catch (IllegalArgumentException ex) {
            throw new CustomNotFoundException("Type with name '" + typeName + "' ");
        }
    }

    public Integer getIntegerFromString(String strNumber) {
        try {
            return Integer.parseInt(strNumber);
        } catch (NullPointerException | NumberFormatException ex) {
            throw new CustomNullPointerException(strNumber);
        }
    }


    //SORTING
    public List<FigureEntity> sortByFields(List<FigureEntity> figureList, String type, String genre, String brand, String material, String startPrice, String endPrice) {

        if (type != null) {
            figureList = sortByType(figureList, type);
        }

        if (genre != null) {
            figureList = sortByGenre(figureList, genre);
        }

        if (brand != null) {
            figureList = sortByBrand(figureList, brand);
        }

        if (material != null) {
            figureList = sortByMaterial(figureList, material);
        }

        if (startPrice != null || endPrice != null) {
            figureList = sortByPriceRange(figureList, startPrice, endPrice);
        }

        return figureList;
    }

    public List<FigureEntity> sortByType(List<FigureEntity> figureList, String typeName) {
        Types type = getTypeFromString(typeName);
        return figureList
                .stream()
                .filter(figure -> figure.getType() != null)
                .filter(figure -> figure.getType().equals(type))
                .toList();
    }

    public List<FigureEntity> sortByGenre(List<FigureEntity> figureList, String genre) {
        return figureList
                .stream()
                .filter(figure -> figure.getAdditionalInfo() != null)
                .filter(figure -> figure.getAdditionalInfo().getGenre() != null)
                .filter(figure -> figure.getAdditionalInfo().getGenre().equals(genre))
                .toList();
    }

    public List<FigureEntity> sortByBrand(List<FigureEntity> figureList, String brand) {
        return figureList
                .stream()
                .filter(figure -> figure.getAdditionalInfo() != null)
                .filter(figure -> figure.getAdditionalInfo().getBrand().equals(brand))
                .toList();
    }

    public List<FigureEntity> sortByMaterial(List<FigureEntity> figureList, String material) {
        return figureList
                .stream()
                .filter(figure -> figure.getAdditionalInfo() != null)
                .filter(figure -> figure.getAdditionalInfo().getMaterial().equals(material))
                .toList();
    }

    public List<FigureEntity> sortByPriceRange(List<FigureEntity> figureList, String startPriceStr, String endPriceStr) {
        if (startPriceStr != null && endPriceStr != null) {
            int startPrice = getIntegerFromString(startPriceStr);
            int endPrice = getIntegerFromString(endPriceStr);

            return figureList
                    .stream()
                    .filter(figure -> startPrice < figure.getCurrentPrice() && endPrice > figure.getCurrentPrice())
                    .toList();
        } else if (startPriceStr != null) {
            int startPrice = getIntegerFromString(startPriceStr);

            return figureList
                    .stream()
                    .filter(figure -> startPrice < figure.getCurrentPrice())
                    .toList();
        } else {
            int endPrice = getIntegerFromString(endPriceStr);

            return figureList
                    .stream()
                    .filter(figure -> endPrice > figure.getCurrentPrice())
                    .toList();
        }
    }


    //PAGINATION
    private Page<FigureEntity> paginateFigureList(List<FigureEntity> figureList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), figureList.size());
        List<FigureEntity> paginatedFigureList = start > end ? new ArrayList<>() : figureList.subList(start, end);
        return new PageImpl<>(paginatedFigureList, pageable, figureList.size());
    }

    private Pageable getPageable(int page, int size) {
        if (page >= 0 && size > 0 && size <= 18) {
            return PageRequest.of(page, size);
        }
        throw new CustomBadRequestException("The pagination elements (page and size values) must satisfy the following: " +
                "page value must be greater than (or equal to) 0, size value must be greater than 0 and less than (or equal to) 18.");
    }


    //REVIEW IN FIGURE
    public void addReviewToFigure(FigureEntity figure, ReviewEntity review) {
        List<ReviewEntity> reviewList = figure.getReviews();
        if (reviewList == null) {
            reviewList = new ArrayList<>();
        }
        reviewList.add(review);
        figure.setReviews(reviewList);
        figureRepository.save(figure);
    }

    public void removeReviewFromFigure(FigureEntity figure, ReviewEntity review) {
        List<ReviewEntity> reviewList = figure.getReviews();

        assert reviewList != null;
        if (reviewList.contains(review)) {
            reviewList.remove(review);
            figure.setReviews(reviewList);
            figureRepository.save(figure);
        }else {
            throw new CustomNotFoundException("Review in the figure's review list", review.getId());
        }
    }


    //FIGURE IN ORDER
    public List<FigureInCartOrderResponseDto> getCartOrderResponseFigures(List<FigureInCartRequestDto> figures) {
        return figures
                .stream()
                .map(figureDto -> {
                    FigureEntity figureEntity = findById(figureDto.id());
                    return new FigureInCartOrderResponseDto(
                            figureEntity.getId(),
                            figureEntity.getName(),
                            figureEntity.getImages().getFirst(),
                            figureDto.amount(),
                            figureEntity.getCurrentPrice()
                    );
                })
                .toList();
    }

    public void updatePurchaseCounter(List<FigureInCartOrderResponseDto> figures) {
        List<FigureEntity> entities = figures
                .stream()
                .map(figureDto -> findById(figureDto.figureId()))
                .toList();

        for (int i = 0; i < figures.size(); i++) {
            FigureEntity entity = entities.get(i);
            entity.setPurchaseCount(entity.getPurchaseCount() + figures.get(i).amount());
        }
    }
}