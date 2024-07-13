package com.teamChallenge.entity.ShoppingCart;

import com.teamChallenge.entity.Figures.FigureEntity;
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

    @Column
    private int price;
}