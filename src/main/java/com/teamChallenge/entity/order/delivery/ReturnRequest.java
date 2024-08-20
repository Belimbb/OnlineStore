package com.teamChallenge.entity.order.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    private String reason;

    private Date date;

    public ReturnRequest(String reason) {
        this.reason = reason;
        date = new Date();
    }
}
