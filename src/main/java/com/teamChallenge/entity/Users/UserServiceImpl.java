package com.teamChallenge.entity.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toDto(userRepository.getReferenceById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        UserEntity newUser = new UserEntity(userDto.username(), userDto.email(), userDto.password());
        userRepository.save(newUser);
        return userMapper.toDto(newUser);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        UserEntity user = userRepository.getReferenceById(id);
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public boolean delete(Long id) {
        userRepository.deleteById(id);
        return true;
    }
}
