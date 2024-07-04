package com.teamChallenge.entity.Users;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    boolean delete(Long id);
}
