package com.teamChallenge.entity.Users;

import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.userExceptions.UserAlreadyExistException;
import com.teamChallenge.exception.exceptions.userExceptions.UserNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService,UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private PasswordEncoder passwordEncoder;
    @Autowired
    public void passwordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getAll() {
        log.info("{}: request on retrieving all users was sent", LogEnum.SERVICE);
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getById(String id) {
        log.info("{}: request on retrieving user by id {} was sent", LogEnum.SERVICE, id);
        return userMapper.toDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
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
    public UserDto create(UserDto userDto) throws UserAlreadyExistException{
        return create(userDto.username(), userDto.email(), userDto.password());
    }

    @Override
    public UserDto create (String username, String email, String password) throws UserAlreadyExistException{
        if (existsByUsername(username)){
            throw new UserAlreadyExistException(username);
        }
        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(password));
        user.setCreatedAt(new Date());
        UserEntity saved = userRepository.save(user);
        log.info("{}: User (Username: {}) was created", LogEnum.SERVICE, username);
        return userMapper.toDto(saved);
    }

    @Override
    public UserDto update(String id, UserDto userDto) {
        UserEntity user = userRepository.findById(id).get();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());

        userRepository.save(user);
        log.info("{}: User (id: {}) was updated", LogEnum.SERVICE, id);
        return userMapper.toDto(user);
    }

    @Override
    public boolean delete(String id) {
        userRepository.deleteById(id);
        log.info("{}: User (id: {}) was deleted", LogEnum.SERVICE, id);
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
        UserEntity user;
        try {
            user = userMapper.toEntity(getByUsername(username));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}