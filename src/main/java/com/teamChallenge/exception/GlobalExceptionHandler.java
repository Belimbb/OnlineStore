package com.teamChallenge.exception;

import com.teamChallenge.exception.exceptions.generalExceptions.*;
import com.teamChallenge.exception.exceptions.userExceptions.UserIncorrectPasswordException;

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

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Map<String, List<String>>> unauthorizedAccessException(UnauthorizedAccessException e) {
        return getErrorsMap(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> notFoundException(CustomNotFoundException e) {
        return getErrorsMap(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomAlreadyExistException.class)
    public ResponseEntity<Map<String, List<String>>> alreadyExistException(CustomAlreadyExistException e) {
        return getErrorsMap(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SomethingWentWrongException.class)
    public ResponseEntity<Map<String, List<String>>> somethingWentWrongException(SomethingWentWrongException ex) {
        return getErrorsMap(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomNullPointerException.class)
    public ResponseEntity<Map<String, List<String>>> nullPointerException(CustomNullPointerException ex) {
        return getErrorsMap(ex, HttpStatus.BAD_REQUEST);
    }

    /* User exceptions */

    @ExceptionHandler(UserIncorrectPasswordException.class)
    public ResponseEntity<Map<String, List<String>>> userIncorrectPasswordException(UserIncorrectPasswordException e) {
        return getErrorsMap(e, HttpStatus.UNAUTHORIZED);
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