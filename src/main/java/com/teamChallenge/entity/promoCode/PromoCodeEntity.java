package com.teamChallenge.entity.promoCode;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "promoCodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private int discount;

    private Date expirationDate;

    public PromoCodeEntity(String code, int discount, Date expirationDate) {
        this.code = code;
        this.discount = discount;
        this.expirationDate = expirationDate;
    }
}
