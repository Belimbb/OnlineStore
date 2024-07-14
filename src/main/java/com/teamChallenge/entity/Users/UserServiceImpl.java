package com.teamChallenge.entity.Users;

import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.userExceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService,UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getById(String id) {
        return userMapper.toDto(userRepository.findById(id).get());
    }

    @Override
    public UserDto getByEmail (String email){
        log.info("{}: request on retrieving user by email {} was sent", LogEnum.SERVICE, email);
        return userMapper.toDto(userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(email)));
    }

    @Override
    public UserDto getByUsername(String username) {
        log.info("{}: request on retrieving user by username {} was sent", LogEnum.SERVICE, username);
        return userMapper.toDto(userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username)));
    }

    @Override
    public UserDto create(UserDto userDto) {
//        UserEntity newUser = new UserEntity(userDto.username(), userDto.email(), userDto.password());
//        userRepository.save(newUser);
//        return userMapper.toDto(newUser);
        return create(userDto.username(), userDto.email(), userDto.password());
    }

    @Override
    public UserDto create (String username, String email, String password){
        UserEntity user = new UserEntity(username, email, password);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(String id, UserDto userDto) {
        UserEntity user = userRepository.findById(id).get();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public boolean delete(String id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existByEmail (String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = null;
        try {
            user = userMapper.toEntity(getByUsername(username));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}