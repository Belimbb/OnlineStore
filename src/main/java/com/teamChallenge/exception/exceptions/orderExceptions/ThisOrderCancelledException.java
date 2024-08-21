package com.teamChallenge.exception.exceptions.orderExceptions;

public class ThisOrderCancelledException extends RuntimeException {

    public static final String THIS_ORDER_CANCELLED_EXCEPTION_TEXT = "This order (id: %s) has already been cancelled. " +
            "You cannot do anything with this order.";

    public ThisOrderCancelledException(String orderId) {
        super(String.format(THIS_ORDER_CANCELLED_EXCEPTION_TEXT, orderId));
    }
}
