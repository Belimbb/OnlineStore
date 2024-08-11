package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
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

    public void setTotalPrice() {
        int totalPrice = 0;
        for (FigureInCartOrderResponseDto figure : figures) {
            totalPrice += figure.price()*figure.amount();
        }
        this.totalPrice = totalPrice;
    }

    public CartEntity(UserEntity user, List<FigureInCartOrderResponseDto> figures) {
        this.user = user;
        this.figures = figures;

        setTotalPrice();
    }
}