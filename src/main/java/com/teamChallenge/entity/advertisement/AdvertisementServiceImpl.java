package com.teamChallenge.entity.advertisement;

import com.teamChallenge.dto.request.AdsRequestDto;
import com.teamChallenge.dto.response.AdsResponseDto;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final AdvertisementMapper advertisementMapper;

    private static final String OBJECT_NAME = "Advertisement";

    @Override
    public AdsResponseDto create(AdsRequestDto adsRequestDto) {
        AdvertisementEntity advertisement = new AdvertisementEntity(adsRequestDto.text(), adsRequestDto.url());
        advertisementRepository.save(advertisement);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was created", LogEnum.SERVICE, advertisement.getId());
        return advertisementMapper.toResponseDto(advertisement);
    }

    @Override
    public AdsResponseDto getById(String id) {
        AdvertisementEntity advertisement = findById(id);
        log.info("{}: " + OBJECT_NAME + " retrieved from db by id {}", LogEnum.SERVICE, id);
        return advertisementMapper.toResponseDto(advertisement);
    }

    @Override
    public List<AdsResponseDto> getAll() {
        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return advertisementMapper.toResponseDtoList(advertisementRepository.findAll());
    }

    @Override
    public AdsResponseDto update(String id, AdsRequestDto adsDto) {
        AdvertisementEntity entity = findById(id);
        entity.setText(adsDto.text());
        entity.setUrl(adsDto.url());

        advertisementRepository.save(entity);
        log.info("{}: " + OBJECT_NAME +" (id: {}) updated)", LogEnum.SERVICE, entity.getId());
        return advertisementMapper.toResponseDto(entity);
    }

    @Override
    public void delete(String id) {
        AdvertisementEntity advertisement = findById(id);
        advertisementRepository.delete(advertisement);
        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }

    private AdvertisementEntity findById(String id) {
        return advertisementRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}