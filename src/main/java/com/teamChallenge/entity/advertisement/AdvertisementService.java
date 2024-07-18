package com.teamChallenge.entity.advertisement;

import java.util.List;

public interface AdvertisementService {
    AdvertisementDto createAds (String text, String url);
    AdvertisementDto getById (String id);
    List<AdvertisementDto> getAll();
    AdvertisementDto updateAds(AdvertisementDto adsDto);
    boolean deleteAds(String id);
}
