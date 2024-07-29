package com.teamChallenge.entity.figure;

import com.teamChallenge.dto.request.figure.FigureRequestDto;
import com.teamChallenge.dto.response.FigureResponseDto;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
    public FigureResponseDto getById(String id){
        FigureEntity figure = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return figureMapper.toResponseDto(figure);
    }

    @Override
    public List<FigureResponseDto> getAllFigures(String filter) {
        if (filter != null) {
            return figureMapper.toResponseDtoList(getFigureListByFilter(filter));
        }

        List<FigureEntity> figureEntities = figureRepository.findAll();
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return figureMapper.toResponseDtoList(figureEntities);
    }

    public List<FigureResponseDto> getAllFiguresByCategory(CategoryEntity category){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findByCategory(category);
        if (figureEntities.isPresent()){
            log.info("{}: All " + OBJECT_NAME + " by category {} retrieved from db", LogEnum.SERVICE, category);
            return figureMapper.toResponseDtoList(figureEntities.get());
        }
        throw new CustomNotFoundException(OBJECT_NAME + "s");
    }

    public List<FigureResponseDto> getAllFiguresBySubCategory (SubCategoryEntity subCategory){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findBySubCategory(subCategory);
        if (figureEntities.isPresent()){
            log.info("{}: All " + OBJECT_NAME + "s by sub category {} retrieved from db", LogEnum.SERVICE, subCategory);
            return figureMapper.toResponseDtoList(figureEntities.get());
        }
        throw new CustomNotFoundException(OBJECT_NAME);
    }

    public List<FigureResponseDto> getFiveBestSellers(){
        Optional<List<FigureEntity>> bestSellers = figureRepository.findFiveBestSellers();
        if (bestSellers.isPresent()){
            log.info("{}: All " + OBJECT_NAME + "s that are best sellers retrieved from db", LogEnum.SERVICE);
            return figureMapper.toResponseDtoList(bestSellers.get());
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

    public List<FigureEntity> getFigureListByFilter(String filter) {
        List<FigureEntity> figureList;

        switch (filter) {
            case "features":
                figureList = getFigureListByLabelsDESC(new Labels[]{Labels.EXCLUSIVE, Labels.LIMITED});
                break;
            default: throw new CustomNotFoundException("filter", filter);
        }

        log.info("{}: All " + OBJECT_NAME + "s (with filter: " + filter + ") retrieved from db", LogEnum.SERVICE);
        return figureList;
    }

    public List<FigureEntity> getFigureListByLabelsDESC(Labels[] labels) {
        List<FigureEntity> figureList = new ArrayList<>();

        for (Labels label : labels) {
            List<FigureEntity> tempFigureList = figureRepository.findByLabel(label, Sort.Direction.DESC);
            figureList.addAll(tempFigureList);
        }

        return figureList
                .stream()
                .distinct()
                .toList();
    }
}
