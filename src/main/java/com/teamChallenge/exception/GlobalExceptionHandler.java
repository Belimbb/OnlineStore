package com.teamChallenge.exception;

import com.teamChallenge.exception.exceptions.figureExceptions.FigureAlreadyExistException;
import com.teamChallenge.exception.exceptions.figureExceptions.FigureNotFoundException;
import com.teamChallenge.exception.exceptions.userExceptions.UserAlreadyExistException;
import com.teamChallenge.exception.exceptions.userExceptions.UserIncorrectPasswordException;
import com.teamChallenge.exception.exceptions.userExceptions.UserNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* Validation exceptions */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> result = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> {
                    result.computeIfAbsent(e.getField(), k -> new ArrayList<>()).add(e.getDefaultMessage());
                });
        return new ResponseEntity<>(getErrorsMap(result), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    /* General exceptions */

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, ExpiredJwtException.class})
    public ResponseEntity<Map<String, List<String>>> unauthorizedAccessException(HttpMessageNotReadableException e) {
        return getErrorsMap(e, HttpStatus.BAD_REQUEST);
    }

    /* User exceptions */

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> userNotFoundException(UserNotFoundException e) {
        return getErrorsMap(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, List<String>>> userAlreadyExistException(UserAlreadyExistException e) {
        return getErrorsMap(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserIncorrectPasswordException.class)
    public ResponseEntity<Map<String, List<String>>> userIncorrectPasswordException(UserIncorrectPasswordException e) {
        return getErrorsMap(e, HttpStatus.UNAUTHORIZED);
    }

    /* Figure exceptions */

    @ExceptionHandler(FigureNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> figureNotFoundException(FigureNotFoundException e) {
        return getErrorsMap(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FigureAlreadyExistException.class)
    public ResponseEntity<Map<String, List<String>>> figureAlreadyExistException(FigureAlreadyExistException e) {
        return getErrorsMap(e, HttpStatus.CONFLICT);
    }

    /* Helpers */

    private Map<String, List<String>> getErrorsMap(Map<String, List<String>> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", new ArrayList<>());
        errors.forEach((key, value) -> {
            value.forEach(v -> errorResponse.get("errors").add(String.format("%s: %s", key, v)));
        });
        return errorResponse;
    }

    private ResponseEntity<Map<String, List<String>>> getErrorsMap(Throwable ex, HttpStatus status) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(map, new HttpHeaders(), status);
    }
}