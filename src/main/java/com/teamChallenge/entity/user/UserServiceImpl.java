package com.teamChallenge.entity.user;

import com.teamChallenge.dto.request.UserRequestDto;
import com.teamChallenge.dto.request.auth.SignupRequestDto;
import com.teamChallenge.dto.response.UserResponseDto;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.review.ReviewEntity;
import com.teamChallenge.entity.address.AddressInfo;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.mail.EmailService;
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

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Value("${admin.email}")
    private String adminEmail;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

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
    public UserResponseDto create (SignupRequestDto signupRequestDto) throws CustomAlreadyExistException {
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();

        if (existsByUsername(username)) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "Username", username);
        }

        if (existByEmail(email)) {
            throw new CustomAlreadyExistException(OBJECT_NAME, "Email",  email);
        }

        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setPasswordVerified(true);
        user.setCreatedAt(new Date());

        if (email.trim().equalsIgnoreCase(adminEmail)){
            user.setRole(Roles.ADMIN);
        }

        UserEntity saved = userRepository.save(user);
        emailService.sendVerificationEmailLetter(email, user.getEmailVerificationCode());
        log.info("{}: " + OBJECT_NAME + " (Username: {}) was created", LogEnum.SERVICE, username);
        return userMapper.toResponseDto(saved);
    }

    @Override
    public UserResponseDto update(String id, UserRequestDto userRequestDto) {
        UserEntity user = findById(id);
        user.setUsername(userRequestDto.username());

        String email = userRequestDto.email();
        String password = userRequestDto.password();

        if (!email.equals(user.getEmail())) {
            if (existByEmail(email)){
                throw new CustomAlreadyExistException(OBJECT_NAME, "Email", email);
            }
            user.setEmail(email);
            user.setEmailVerified(false);
            user.setEmailVerificationCode(UUID.randomUUID().toString().substring(0, 6));
        }
        if (!passwordEncoder.matches(password, user.getPassword())){
            user.setPassword(passwordEncoder.encode(password));
            user.setPasswordVerified(false);
            user.setPasswordVerificationCode(UUID.randomUUID().toString().substring(0, 6));
        }

        String phoneNumber = userRequestDto.phoneNumber();
        if (phoneNumber != null && !phoneNumber.isBlank() && !existsByPhoneNumber(phoneNumber)) {
            user.setPhoneNumber(phoneNumber);
        }

        UserEntity updatedUser = userRepository.save(user);
        if (!user.isEmailVerified()) {
            emailService.sendVerificationEmailLetter(email, updatedUser.getEmailVerificationCode());
        }
        if (!user.isPasswordVerified()){
            emailService.sendVerificationPasswordLetter(email, updatedUser.getPasswordVerificationCode());
        }


        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateAddressInfo(String id, AddressInfo addressInfo) {
        UserEntity user = findById(id);
        user.setAddressInfo(addressInfo);

        UserEntity updatedUser = userRepository.save(user);
        log.info("{}: " + OBJECT_NAME + "'s (id: {}) address info field was updated", LogEnum.SERVICE, id);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public boolean delete(String id) {
        UserEntity user = findById(id);
        userRepository.delete(user);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
        return true;
    }

    public void addFigureToWishList(FigureEntity figure) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = findByEmail(email);
        List<FigureEntity> whishList = user.getWhishList();
        if (whishList == null){
            whishList = new ArrayList<>();
        }
        whishList.add(figure);
        user.setWhishList(whishList);
        userRepository.save(user);
    }

    public FigureEntity getFigureFromWishList(UserEntity user, String figureId){
        for (FigureEntity entity:user.getWhishList()){
            if (entity.getId().equals(figureId)){
                return entity;
            }
        }
        throw new CustomNotFoundException(figureId);
    }

    @Override
    public void removeFigureFromWishList(String figureId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = findByEmail(email);
        user.getWhishList().remove(getFigureFromWishList(user, figureId));
        userRepository.save(user);
    }

    public UserResponseDto sendEmilVerifMessage(String email){
        UserEntity user = findByEmail(email);

        if (user.getEmailVerificationCode()==null){
            user.setEmailVerificationCode(UUID.randomUUID().toString().substring(0, 6));
        }

        emailService.sendVerificationEmailLetter(email, user.getEmailVerificationCode());
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto sendPasswordVerifMessage(String email){
        UserEntity user = findByEmail(email);

        if (user.getPasswordVerificationCode()==null){
            user.setPasswordVerificationCode(UUID.randomUUID().toString().substring(0, 6));
        }

        emailService.sendVerificationPasswordLetter(email, user.getPasswordVerificationCode());
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto confirmEmail(String emailVerificationCode) {
        UserEntity user = findByEmailVerificationCode(emailVerificationCode);
        user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        log.info("{}: " + OBJECT_NAME + "'s (id: {}) email has been confirmed", LogEnum.SERVICE, user.getId());
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    public UserResponseDto confirmPassword(String passwordVerificationCode) {
        UserEntity user = findByPasswordVerificationCode(passwordVerificationCode);
        user.setPasswordVerified(true);
        user.setPasswordVerificationCode(null);
        log.info("{}: " + OBJECT_NAME + "'s (id: {}) password has been confirmed", LogEnum.SERVICE, user.getId());
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    public boolean existByEmail (String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
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

    public UserEntity findByEmailVerificationCode(String verificationCode) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by email verification code {} was sent", LogEnum.SERVICE, verificationCode);
        return userRepository.findByEmailVerificationCode(verificationCode).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME));
    }

    public UserEntity findByPasswordVerificationCode(String verificationCode) {
        log.info("{}: request on retrieving " + OBJECT_NAME + " by password verification code {} was sent", LogEnum.SERVICE, verificationCode);
        return userRepository.findByPasswordVerificationCode(verificationCode).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME));
    }

    public void addFigureToRecentlyViewedList(String userEmail, FigureEntity figure) {
        UserEntity user = findByEmail(userEmail);
        List<FigureEntity> recentlyViewedList = user.getRecentlyViewed();

        if (recentlyViewedList == null) {
            recentlyViewedList = new ArrayList<>();
        }

        if (recentlyViewedList.size() == 5) {
            recentlyViewedList.removeLast();
        }

        if (!recentlyViewedList.contains(figure)) {
            recentlyViewedList.addFirst(figure);
            user.setRecentlyViewed(recentlyViewedList);
            userRepository.save(user);
            log.info("{}: add figure (figureId: {}) to " + OBJECT_NAME + " (userId: {}) recently viewed list was sent", LogEnum.SERVICE, figure.getId(), user.getId());
        }
    }

    public void addReviewToUser(UserEntity user, ReviewEntity review) {
        List<ReviewEntity> reviewList = user.getReviews();
        if (reviewList==null){
            reviewList = new ArrayList<>();
        }
        reviewList.add(review);
        user.setReviews(reviewList);
        userRepository.save(user);
    }

    public void removeReviewFromUser(UserEntity user, ReviewEntity review) {
        List<ReviewEntity> reviewList = user.getReviews();

        assert reviewList != null;
        if (reviewList.contains(review)) {
            reviewList.remove(review);
            user.setReviews(reviewList);
            userRepository.save(user);
        }else {
            throw new CustomNotFoundException("Review in the user's review list", review.getId());
        }
    }
}