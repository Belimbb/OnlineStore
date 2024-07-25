package com.teamChallenge.entity.shoppingCart;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;
import jakarta.persistence.*;

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

    @DBRef
    private List<FigureEntity> figures;

    @DBRef
    private UserEntity user;

    @Column
    private int price;

    public CartEntity(UserEntity user, int price, List<FigureEntity> figureList) {
        this.user = user;
        this.price = price;
        this.figures = figureList;
    }

    public CartEntity(List<FigureEntity> figures) {
        this.figures = figures;
    }
}