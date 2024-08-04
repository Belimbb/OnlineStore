package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Document(collection = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity implements Serializable {

    @Id
    private String id;

    @Column(nullable = false)
    private Map<String, Integer> figureIdAndAmountMap;

    @DBRef
    private UserEntity user;

    @Column(nullable = false)
    private int price;

    public CartEntity(UserEntity user, int price, Map<String, Integer> figureIdAndAmountMap) {
        this.user = user;
        this.price = price;
        this.figureIdAndAmountMap = figureIdAndAmountMap;
    }
}