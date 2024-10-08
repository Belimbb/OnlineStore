package com.teamChallenge.entity.user;

import com.teamChallenge.dto.request.UserRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.address.AddressInfo;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAll();
    UserResponseDto getById(String id);
    UserResponseDto create(SignupRequestDto signupRequestDto) throws CustomAlreadyExistException;
    UserResponseDto update(String id, UserRequestDto userRequestDto);
    UserResponseDto updateAddressInfo(String id, AddressInfo addressInfo);
    boolean delete(String id);
    void removeFigureFromWishList(String figureId);
}