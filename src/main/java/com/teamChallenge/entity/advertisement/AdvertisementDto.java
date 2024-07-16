package com.teamChallenge.entity.advertisement;

public record AdvertisementDto(String id, String text, String url) {
    public AdvertisementDto(String text, String url) {
        this(null, text, url);
    }
}
