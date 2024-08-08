package com.teamChallenge.entity.review;

import com.teamChallenge.dto.request.ReviewRequestDto;
import com.teamChallenge.dto.response.ReviewResponseDto;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.figure.FigureServiceImpl;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final UserServiceImpl userService;

    private final FigureServiceImpl figureService;

    private static final String OBJECT_NAME = "review";

    @Override
    public List<ReviewResponseDto> getAll() {
        List<ReviewEntity> reviewEntityList = reviewRepository.findAll();
        log.info("{}: request on retrieving all " + OBJECT_NAME + "s was sent", LogEnum.SERVICE);
        return reviewMapper.toResponseDtoList(reviewEntityList);
    }

    @Override
    public ReviewResponseDto getById(String id) {
        ReviewEntity review = findById(id);
        log.info("{}: request on retrieving " + OBJECT_NAME + " by id {} was sent", LogEnum.SERVICE, id);
        return reviewMapper.toResponseDto(review);
    }

    @Override
    public ReviewResponseDto create(ReviewRequestDto reviewRequestDto) {
        UserEntity user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        FigureEntity figure = figureService.findById(reviewRequestDto.figureId());

        if (reviewRepository.existsByUserAndFigure(user, figure)) {
            throw new CustomAlreadyExistException(String.format("This user (email: %s) has already written a review for the figure (name: %s)", user.getEmail(), figure.getName()));
        }

        ReviewEntity newReview = new ReviewEntity(reviewRequestDto.score(), user, reviewRequestDto.advantages(), reviewRequestDto.disadvantages(), figure);
        newReview.setCreationDate(new Date());

        ReviewEntity savedReview = reviewRepository.save(newReview);
        figureService.addReviewToFigure(figure, savedReview);
        userService.addReviewToUser(user, savedReview);

        log.info("{}: " + OBJECT_NAME + " (Id: {}) was created", LogEnum.SERVICE, savedReview.getId());
        return reviewMapper.toResponseDto(savedReview);
    }

    @Override
    public ReviewResponseDto update(String id, ReviewRequestDto reviewRequestDto) {
        ReviewEntity review = findById(id);
        review.setScore(reviewRequestDto.score());
        review.setAdvantages(reviewRequestDto.advantages());
        review.setDisadvantages(reviewRequestDto.disadvantages());

        ReviewEntity savedReview = reviewRepository.save(review);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was updated", LogEnum.SERVICE, id);
        return reviewMapper.toResponseDto(savedReview);
    }

    @Override
    public void delete(String id) {
        ReviewEntity review = findById(id);
        FigureEntity figure = review.getFigure();
        UserEntity user = review.getUser();

        reviewRepository.delete(review);
        figureService.removeReviewFromFigure(figure, review);
        userService.removeReviewFromUser(user, review);
        log.info("{}: " + OBJECT_NAME + " (id: {}) was deleted", LogEnum.SERVICE, id);
    }

    public ReviewEntity findById(String id) {
        return reviewRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(OBJECT_NAME, id));
    }
}
