package com.teamChallenge.entity.Figures;

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
    public FigureDto createFigure(String name, String shortDescription, String longDescription, Enum<?> subCategory, int price, int amount, String color, List<String> images){
        FigureEntity figureEntity = new FigureEntity(name, shortDescription, longDescription,
                subCategory, price, amount, color, images);
        figureRepository.save(figureEntity);
        return figureMapper.toDto(figureEntity);
    }

    @Override
    public FigureDto getById(String id){
        Optional<FigureEntity> figureEntity = figureRepository.findById(id);
        if (figureEntity.isPresent()){
            return figureMapper.toDto(figureEntity.get());
        }
        throw new FigureNotFoundException(id);
    }

    @Override
    public List<FigureDto> getAllFigures() {
        List<FigureEntity> figureEntities = figureRepository.findAll();
        return figureMapper.toDtoList(figureEntities);
    }

    public List<FigureDto> getAllFiguresByCategory(String category){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findByCategory(category);
        if (figureEntities.isPresent()){
            return figureMapper.toDtoList(figureEntities.get());
        }
        throw new FigureNotFoundException();
    }

    public List<FigureDto> getAllFiguresBySubCategory (Enum<?> subCategory){
        Optional<List<FigureEntity>> figureEntities = figureRepository.findBySubCategory(subCategory);
        if (figureEntities.isPresent()){
            return figureMapper.toDtoList(figureEntities.get());
        }
        throw new FigureNotFoundException();
    }

    @Override
    public FigureDto updateFigure(FigureDto figureDto) {
        FigureEntity savedFigure = figureRepository.save(figureMapper.toEntity(figureDto));
        return figureMapper.toDto(savedFigure);
    }

    @Override
    public boolean deleteFigure(String id) {
        if(figureRepository.existsById(id)){
            figureRepository.deleteById(id);
            return true;
        }
        throw new FigureNotFoundException(id);
    }
}
