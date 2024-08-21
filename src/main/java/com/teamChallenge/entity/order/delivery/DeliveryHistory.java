package com.teamChallenge.entity.order.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DeliveryHistory {

    private boolean isPlaced, isPlacedProcessed, isSent, isDelivered, isReturnRequested, isReturnRequestProcessed, isReturned, isRefunded, isFinished, isCancelled;

    private String statusDescription;

    private Date dateOfPlaced, dateOfPlacedProcessed, dateOfSent, dateOfDelivered, dateOfReturnRequested, dateOfReturnRequestProcessed,
            dateOfReturned, dateOfRefunded, dateOfFinished, dateOfCancelled;

    public DeliveryHistory() {
        isPlaced = true;
        statusDescription = "we are processing your order";
        dateOfPlaced = new Date();
    }
}
