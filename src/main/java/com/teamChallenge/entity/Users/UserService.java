package com.teamChallenge.entity.Users;

import com.teamChallenge.exception.exceptions.userExceptions.UserAlreadyExistException;
import com.teamChallenge.exception.exceptions.userExceptions.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();
    UserDto getById(String id);
    UserDto getByEmail(String email) throws UserNotFoundException;
    UserDto getByUsername(String username);

    UserDto create(UserDto userDto) throws UserAlreadyExistException;
    UserDto create(String username, String email, String password) throws UserAlreadyExistException;
    UserDto update(String id, UserDto userDto);

    boolean delete(String id);
    boolean existByEmail(String email);
    boolean existsByUsername(String username);
}