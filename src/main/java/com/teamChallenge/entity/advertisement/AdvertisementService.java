package com.teamChallenge.entity.advertisement;

import com.teamChallenge.dto.request.AdsRequestDto;
import com.teamChallenge.dto.response.AdsResponseDto;

import java.util.List;

public interface AdvertisementService {
    AdsResponseDto createAds (String text, String url);
    AdsResponseDto getById (String id);
    List<AdsResponseDto> getAll();
    AdsResponseDto updateAds(AdsRequestDto adsDto);
    void deleteAds(String id);
}
