package com.teamChallenge.entity.promoCode;

import com.teamChallenge.dto.request.PromoCodeRequestDto;
import com.teamChallenge.dto.response.PromoCodeResponseDto;

import java.util.List;

public interface PromoCodeService {

    List<PromoCodeResponseDto> getAll();

    PromoCodeResponseDto getById(String id);

    PromoCodeResponseDto getByCode(String code);

    PromoCodeResponseDto create(PromoCodeRequestDto requestDto);

    PromoCodeResponseDto update(String id, PromoCodeRequestDto requestDto);

    void delete(String id);
}
