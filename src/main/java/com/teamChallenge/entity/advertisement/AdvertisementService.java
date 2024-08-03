package com.teamChallenge.entity.advertisement;

import com.teamChallenge.dto.request.AdsRequestDto;
import com.teamChallenge.dto.response.AdsResponseDto;

import java.util.List;

public interface AdvertisementService {
    AdsResponseDto create(AdsRequestDto adsRequestDto);
    AdsResponseDto getById (String id);
    List<AdsResponseDto> getAll();
    AdsResponseDto update(AdsRequestDto adsDto);
    void delete(String id);
}
