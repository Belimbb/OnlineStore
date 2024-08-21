package com.teamChallenge.exception.exceptions.orderExceptions;

public class UpdateDeliveryStatusException extends RuntimeException {

    public static final String UPDATE_DELIVERY_STATUS_EXCEPTION_TEXT = "Request doesn't appear to be valid. " +
            "Please try to change the current delivery status (%s) or contact the administrator.";

    public UpdateDeliveryStatusException(String currentDeliveryStatus) {
        super(String.format(UPDATE_DELIVERY_STATUS_EXCEPTION_TEXT, currentDeliveryStatus));
    }
}