package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.Labels;
import com.teamChallenge.entity.figure.sections.SubCategory;
import com.teamChallenge.exception.LogEnum;

import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigureServiceImpl implements FigureService{

    private final FigureRepository figureRepository;

    private final FigureMapper figureMapper;

    private static final String OBJECT_NAME = "Figure";

    @Override
    public FigureDto createFigure(String name, String shortDescription, String longDescription, SubCategory subCategory, Labels label, int currentPrice, int oldPrice, int amount, String color, List<String> images) throws CustomAlreadyExistException {
        FigureEntity figureEntity = new FigureEntity(name, shortDescription, longDescription,
                subCategory, null,false, currentPrice, oldPrice, amount, color, images);

        if (figureRepository.existsByUniqueHash(figureEntity.getUniqueHash())){
            throw new CustomAlreadyExistException(OBJECT_NAME, name);
        }
        figureEntity.setCreatedAt(new Date());
        figureRepository.save(figureEntity);
        log.info("{}: " + OBJECT_NAME + " (Name: {}) was created", LogEnum.SERVICE, name);
        return figureMapper.toDto(figureEntity);
    }

    @Override
    public FigureDto getById(String id){
        FigureEntity figure = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return figureMapper.toDto(figure);
    }

    @Override
    public List<FigureDto> getAllFigures() {
        List<FigureEntity> figureEntities = figureRepository.findAll();
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return figureMapper.toDtoList(figureEntities);
    }

    public List<FigureDto> getAllFiguresByCategory(Category category){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findByCategory(category);
        if (figureEntities.isPresent()){
            log.info("{}: All " + OBJECT_NAME + " by category {} retrieved from db", LogEnum.SERVICE, category);
            return figureMapper.toDtoList(figureEntities.get());
        }
        throw new CustomNotFoundException(OBJECT_NAME + "s");
    }

    public List<FigureDto> getAllFiguresBySubCategory (SubCategory subCategory){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findBySubCategory(subCategory);
        if (figureEntities.isPresent()){
            log.info("{}: All " + OBJECT_NAME + "s by sub category {} retrieved from db", LogEnum.SERVICE, subCategory);
            return figureMapper.toDtoList(figureEntities.get());
        }
        throw new CustomNotFoundException(OBJECT_NAME);
    }

    @Override
    public FigureDto updateFigure(FigureDto figureDto) {
        FigureEntity savedFigure = figureRepository.save(figureMapper.toEntity(figureDto));
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, savedFigure.getId());
        return figureMapper.toDto(savedFigure);
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
}
