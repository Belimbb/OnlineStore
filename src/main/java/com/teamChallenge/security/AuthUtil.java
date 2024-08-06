package com.teamChallenge.security;

import com.teamChallenge.entity.user.Roles;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.UserServiceImpl;
import com.teamChallenge.exception.exceptions.generalExceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserServiceImpl userService;

    public void validateAdminRole(Principal principal) throws UnauthorizedAccessException {
        if (!extractUser(principal).getRole().equals(Roles.ADMIN)) {
            throw new UnauthorizedAccessException();
        }
    }
    public boolean isAdmin(Principal principal) {
        return extractUser(principal).getRole().equals(Roles.ADMIN);
    }

    private UserEntity extractUser(Principal principal){
        return userService.findByEmail(principal.getName());
    }
}
