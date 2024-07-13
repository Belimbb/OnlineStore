package com.teamChallenge.entity.Users;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    UserDto getById(String id);

    UserDto create(UserDto userDto);

    UserDto update(String id, UserDto userDto);

    boolean delete(String id);
}
