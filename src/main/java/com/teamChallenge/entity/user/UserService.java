package com.teamChallenge.entity.user;

import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();
    UserDto getById(String id);
    UserDto getByUsername(String username);

    UserDto create(UserDto userDto) throws CustomAlreadyExistException;
    UserDto create(String username, String email, String password) throws CustomAlreadyExistException;
    UserDto update(String id, UserDto userDto);

    boolean delete(String id);
    boolean existByEmail(String email);
    boolean existsByUsername(String username);
}