package com.teamChallenge.entity.Products;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class Product {

    String name, description;

    int price, amount;

    List<String> images;
}
