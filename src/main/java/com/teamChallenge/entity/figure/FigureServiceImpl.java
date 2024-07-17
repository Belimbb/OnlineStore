package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Category;
import com.teamChallenge.entity.figure.sections.SubCategory;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.figureExceptions.FigureNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigureServiceImpl implements FigureService{

    private final FigureRepository figureRepository;
    private final FigureMapper figureMapper;

    @Override
    public FigureDto createFigure(String name, String shortDescription, String longDescription, SubCategory subCategory, int price, int amount, String color, List<String> images){
        FigureEntity figureEntity = new FigureEntity(name, shortDescription, longDescription,
                subCategory, price, amount, color, images);
        figureRepository.save(figureEntity);
        log.info("{}: Figure (Name: {}) was created", LogEnum.SERVICE, name);
        return figureMapper.toDto(figureEntity);
    }

    @Override
    public FigureDto getById(String id){
        Optional<FigureEntity> figureEntity = figureRepository.findById(id);
        if (figureEntity.isPresent()){
            log.info("{}: Figure retrieved from db by id {}", LogEnum.SERVICE, id);
            return figureMapper.toDto(figureEntity.get());
        }
        throw new FigureNotFoundException(id);
    }

    @Override
    public List<FigureDto> getAllFigures() {
        List<FigureEntity> figureEntities = figureRepository.findAll();
        log.info("{}: All figures retrieved from db", LogEnum.SERVICE);
        return figureMapper.toDtoList(figureEntities);
    }

    public List<FigureDto> getAllFiguresByCategory(Category category){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findByCategory(category);
        if (figureEntities.isPresent()){
            log.info("{}: All figures by category {} retrieved from db", LogEnum.SERVICE, category);
            return figureMapper.toDtoList(figureEntities.get());
        }
        throw new FigureNotFoundException();
    }

    public List<FigureDto> getAllFiguresBySubCategory (SubCategory subCategory){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findBySubCategory(subCategory);
        if (figureEntities.isPresent()){
            log.info("{}: All figures by sub category {} retrieved from db", LogEnum.SERVICE, subCategory);
            return figureMapper.toDtoList(figureEntities.get());
        }
        throw new FigureNotFoundException();
    }

    @Override
    public FigureDto updateFigure(FigureDto figureDto) {
        FigureEntity savedFigure = figureRepository.save(figureMapper.toEntity(figureDto));
        log.info("{}: Figure (id: {}) updated)", LogEnum.SERVICE, savedFigure.getId());
        return figureMapper.toDto(savedFigure);
    }

    @Override
    public boolean deleteFigure(String id) {
        if(figureRepository.existsById(id)){
            figureRepository.deleteById(id);
            log.info("{}: Figure (id: {}) deleted", LogEnum.SERVICE, id);
            return true;
        }
        throw new FigureNotFoundException(id);
    }
}
