package com.teamChallenge.entity.banner;

import com.teamChallenge.dto.request.BannerRequestDto;
import com.teamChallenge.dto.response.BannerResponseDto;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BannerServiceImpl implements BannerService{
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private static final String OBJECT_NAME = "Banner";

    @Override
    public BannerResponseDto create(BannerRequestDto dto) throws CustomAlreadyExistException {
        BannerEntity entity = bannerMapper.toEntity(dto);
        String title = entity.getTitle();

        if (bannerRepository.existsByUniqueHash(entity.getUniqueHash())){
            throw new CustomAlreadyExistException(OBJECT_NAME, title);
        }

        BannerEntity saved = bannerRepository.save(entity);
        log.info("{}: " + OBJECT_NAME + " (Title: {}) was created", LogEnum.SERVICE, title);
        return bannerMapper.toResponseDto(saved);
    }

    @Override
    public BannerResponseDto getById(String id) throws CustomNotFoundException {
        BannerEntity entity = findById(id);

        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return bannerMapper.toResponseDto(entity);
    }

    @Override
    public List<BannerResponseDto> getAll() {
        List<BannerEntity> entityList = bannerRepository.findAll();
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return bannerMapper.toResponseDtoList(entityList);
    }

    @Override
    public BannerResponseDto update(String id, BannerRequestDto dto) throws CustomNotFoundException {
        if (!bannerRepository.existsById(id)){
            throw new CustomAlreadyExistException(OBJECT_NAME, dto.title());
        }

        BannerEntity entity = bannerMapper.toEntity(dto);
        entity.setId(id);
        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, entity.getId());
        return bannerMapper.toResponseDto(bannerRepository.save(entity));
    }

    @Override
    public void delete(String id) throws CustomNotFoundException {
        if (bannerRepository.existsById(id)){
            bannerRepository.deleteById(id);
        } else {
            throw new CustomNotFoundException(OBJECT_NAME, id);
        }
    }

    public BannerEntity findById(String id) {
        return bannerRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}