package com.teamChallenge.entity.Products;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
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

    Category category;

    int price, amount;

    List<String> images;
}
