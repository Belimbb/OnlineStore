package com.teamChallenge.entity.user;

import com.teamChallenge.dto.request.UserRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class UserServiceImpl implements UserDetailsService, UserService {
    @Value("${admin.email}")
    private String adminEmail;

    private final UserRepository userRepository;
    private final FigureServiceImpl figureService;

    private final UserMapper userMapper;

    private static final String OBJECT_NAME = "user";

    private PasswordEncoder passwordEncoder;
    @Autowired
    public void passwordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDto> getAll() {
        log.info("{}: request on retrieving all " + OBJECT_NAME + "s was sent", LogEnum.SERVICE);
        return userMapper.toResponseDtoList(userRepository.findAll());
    }

    @Override
    public UserResponseDto getById(String id) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by id {} was sent", LogEnum.SERVICE, id);
        return userMapper.toResponseDto(findById(id));
    }

    @Override
    public UserResponseDto create (SignupRequestDto signupRequestDto) throws CustomAlreadyExistException{
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();

        if (existsByUsername(username)) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "Username", username);
        }

        if (existByEmail(email)) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "Email",  email);
        }

        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setCreatedAt(new Date());

        if (email.trim().equalsIgnoreCase(adminEmail)){
            user.setRole(Roles.ADMIN);
        }

        UserEntity saved = userRepository.save(user);
        log.info("{}: " + OBJECT_NAME + " (Username: {}) was created", LogEnum.SERVICE, username);
        return userMapper.toResponseDto(saved);
    }

    @Override
    public UserResponseDto update(String id, UserRequestDto userRequestDto) {
        UserEntity user = findById(id);
        user.setUsername(userRequestDto.username());
        user.setPassword(userRequestDto.password());

        userRepository.save(user);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return userMapper.toResponseDto(user);
    }

    @Override
    public boolean delete(String id) {
        UserEntity user = findById(id);
        userRepository.delete(user);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
        return true;
    }

    public UserResponseDto addFigureToWishList(String email, String figureId) {
        FigureEntity figure = figureService.findById(figureId);
        UserEntity user = findByEmail(email);
        user.getWhishList().add(figure);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    public FigureEntity getFigureFromWishList(UserEntity user, String figureId){
        for (FigureEntity entity:user.getWhishList()){
            if (entity.getId().equals(figureId)){
                return entity;
            }
        }
        throw new CustomNotFoundException(figureId);
    }

    public void removeFigureFromWishList(String email, String figureId) {
        UserEntity user = findByEmail(email);
        user.getWhishList().remove(getFigureFromWishList(user, figureId));
        userRepository.save(user);
    }

    public boolean existByEmail (String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user;
        try {
            user = findByEmail(email);
        } catch (CustomNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    private UserEntity findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }

    public UserEntity findByEmail (String email) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by email {} was sent", LogEnum.SERVICE, email);
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, email));
    }

    public UserEntity getByUsername(String username) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by username {} was sent", LogEnum.SERVICE, username);
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, username));
    }
}