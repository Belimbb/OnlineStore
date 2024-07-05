package com.teamChallenge.entity.Products;

import com.teamChallenge.entity.Orders.OrderEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Используйте AUTO для автоматического определения провайдера
    private UUID id;

    String name, shortDescription, longDescription;

    Enum category;

    int price, amount;

    List<String> images;

    @ManyToMany
    List<OrderEntity> orders;
}
