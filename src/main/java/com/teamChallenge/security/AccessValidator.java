package com.teamChallenge.security;

import com.teamChallenge.dto.request.ReplyRequestDto;
import com.teamChallenge.entity.order.OrderEntity;
import com.teamChallenge.entity.review.ReviewEntity;
import com.teamChallenge.entity.review.reply.Reply;
import com.teamChallenge.entity.shoppingCart.CartServiceImpl;
import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AccessValidator {
    private final UserServiceImpl userService;
    private final CartServiceImpl cartService;

    public void hasPermission(String objectName, String id) throws UnauthorizedAccessException {
        UserEntity user = getUser();

        try {
            isAdmin(user);
            return;
        }catch (UnauthorizedAccessException ignored){

        }

        switch (objectName){
            case "user": if (user.getId().equals(id)){
                break;
            }else {
                throw new UnauthorizedAccessException();
            }
            case "cart": if (cartService.findById(id).getUser().getId().equals(user.getId())){
                break;
            }else {
                throw new UnauthorizedAccessException();
            }
            case "order": for (OrderEntity elem:user.getOrderHistory()){
                if (elem.getId().equals(id)){
                    break;
                }
            }
            throw new UnauthorizedAccessException();

            case "review": for (ReviewEntity elem:user.getReviews()){
                if (elem.getId().equals(id)){
                    break;
                }
            }
            throw new UnauthorizedAccessException();

            default:throw new CustomNotFoundException("Object for AccessValidator");
        }
    }

    public void ownReply(String reviewId, ReplyRequestDto replyDto) throws UnauthorizedAccessException {
        UserEntity user = getUser();
        Reply newReply = new Reply(user.getUsername(), replyDto.text(), replyDto.likes(), replyDto.dislikes());

        ReviewEntity review = user.getReviews().stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(UnauthorizedAccessException::new);

        if (!review.getReplies().contains(newReply)) {
            throw new UnauthorizedAccessException();
        }
    }

    public void isAdmin() throws UnauthorizedAccessException {
        isAdmin(getUser());
    }

    private void isAdmin(UserEntity user) throws UnauthorizedAccessException {
        if (!user.getRole().equals(Roles.ADMIN)){
            throw new UnauthorizedAccessException();
        }
    }

    private UserEntity getUser(){
        return userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}