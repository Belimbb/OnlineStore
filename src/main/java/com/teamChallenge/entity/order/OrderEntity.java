package com.teamChallenge.entity.order;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;
import com.teamChallenge.entity.user.address.AddressInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private AddressInfo address;

    @Column(nullable = false)
    private int price;

    @Enumerated
    private Statuses status;

    @DBRef
    private List<FigureEntity> figureList;

    @DBRef
    private UserEntity user;

    public OrderEntity(AddressInfo address, int price, List<FigureEntity> figureList, UserEntity user) {
        this.address = address;
        this.price = price;
        status = Statuses.NEW;
        this.figureList = figureList;
        this.user = user;
    }
}
