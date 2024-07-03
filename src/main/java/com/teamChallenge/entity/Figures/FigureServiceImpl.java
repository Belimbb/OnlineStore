package com.teamChallenge.entity.Figures;

import com.teamChallenge.exception.exceptions.productExceptions.ProductAlreadyExistException;
import com.teamChallenge.exception.exceptions.productExceptions.ProductNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FigureServiceImpl implements FigureService{

    private final FigureRepository figureRepository;
    private final FigureMapper figureMapper;

    @Override
    public FigureDto createFigure(String name, String shortDescription, String longDescription, Enum category, int price, int amount, String color, List<String> images) throws ProductAlreadyExistException {
        FigureEntity figureEntity = new FigureEntity(name, shortDescription, longDescription,
                category, price, amount, color, images);
        figureRepository.save(figureEntity);
        return figureMapper.toDto(figureEntity);
    }

    @Override
    public FigureDto getById(UUID id){
        Optional<FigureEntity> figureEntity = figureRepository.findById(id);
        if (figureEntity.isPresent()){
            return figureMapper.toDto(figureEntity.get());
        }
        throw new ProductNotFoundException(id);
    }

    @Override
    public List<FigureDto> getAllFigures() {
        List<FigureEntity> figureEntities = figureRepository.findAll();
        return figureMapper.toDtoList(figureEntities);
    }

    @Override
    public FigureDto updateFigure(FigureDto figureDto) {
        FigureEntity savedFigure = figureRepository.save(figureMapper.toEntity(figureDto));
        return figureMapper.toDto(savedFigure);
    }

    @Override
    public boolean deleteFigure(UUID id) {
        if(figureRepository.existById(id)){
            return figureRepository.deleteById(id);
        }
        throw new ProductNotFoundException(id);
    }
}
