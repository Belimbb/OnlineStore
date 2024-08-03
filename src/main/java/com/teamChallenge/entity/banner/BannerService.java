package com.teamChallenge.entity.banner;

import com.teamChallenge.dto.request.BannerRequestDto;
import com.teamChallenge.dto.response.BannerResponseDto;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import java.util.List;

public interface BannerService {
    BannerResponseDto create (BannerRequestDto dto) throws CustomAlreadyExistException;
    BannerResponseDto getById (String id) throws CustomNotFoundException;
    List<BannerResponseDto> getAll ();
    BannerResponseDto update (String id, BannerRequestDto dto) throws CustomNotFoundException;
    void delete (String id) throws CustomNotFoundException;
}