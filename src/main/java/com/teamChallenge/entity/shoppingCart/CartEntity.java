package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.response.PromoCodeResponseDto;
import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.entity.promoCode.PromoCodeEntity;
import com.teamChallenge.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity implements Serializable {

    @Id
    private String id;

    private List<FigureInCartOrderResponseDto> figures;

    @DBRef
    private UserEntity user;

    @Column(nullable = false)
    private int totalPrice;

    public void setTotalPrice(int discount) {
        int totalPrice = -discount;
        for (FigureInCartOrderResponseDto figure : figures) {
            totalPrice += figure.price()*figure.amount();

            if (totalPrice<0){
                totalPrice = 0;
                break;
            }
        }
        this.totalPrice = totalPrice;
    }

    public CartEntity(UserEntity user, List<FigureInCartOrderResponseDto> figures, int discount) {
        this.user = user;
        this.figures = figures;

        setTotalPrice(discount);
    }
}