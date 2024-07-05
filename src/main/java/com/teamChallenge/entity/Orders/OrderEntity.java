package com.teamChallenge.entity.Orders;

import com.teamChallenge.entity.Figures.FigureEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int price;

    @Enumerated
    private Statuses status;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    private List<FigureEntity> figureList;

    public OrderEntity(String address, int price, List<FigureEntity> figureList) {
        this.address = address;
        this.price = price;
        status = Statuses.NEW;
        this.figureList = figureList;
    }
}
