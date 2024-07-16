package com.teamChallenge.exception.exceptions.orderExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    private static final String ORDER_NOT_FOUND_EXCEPTION_TEXT = "Order not found";
    private static final String ORDER_WITH_ID_NOT_FOUND_EXCEPTION_TEXT = "Order with id %s not found.";

    public OrderNotFoundException() {
        super(ORDER_NOT_FOUND_EXCEPTION_TEXT);
    }

    public OrderNotFoundException(String id) {
        super(String.format(ORDER_WITH_ID_NOT_FOUND_EXCEPTION_TEXT, id));
    }
}
