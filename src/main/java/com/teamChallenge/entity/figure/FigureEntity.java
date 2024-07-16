package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.Orders.OrderEntity;
import com.teamChallenge.entity.ShoppingCart.CartEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "figures")
@Data
@NoArgsConstructor
public class FigureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Используйте AUTO для автоматического определения провайдера
    private UUID id;

    @Size(max = 50)
    @Column (nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @Size(min = 10, max = 200)
    @Column (nullable = false)
    private String shortDescription;

    @Size(min = 10, max = 1000)
    @Column (nullable = false)
    private String longDescription;

    @Column (nullable = false)
    private String category;

    @Column (nullable = false)
    private Enum<?> subCategory;

    @Column (nullable = false)
    int price;

    @Column (nullable = false)
    int amount;

    @Column(nullable = false)
    private String color;

    @Column
    private List<String> images;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;

    @ManyToMany
    private List<OrderEntity> orders;

    public FigureEntity(String name, String shortDescription, String longDescription, Enum<?> subCategory, int price, int amount, String color, List<String> images) {
        this.setName(name);
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setCategory(subCategory.getClass().getSimpleName());
        this.setSubCategory(subCategory);
        this.setPrice(price);
        this.setAmount(amount);
        this.setColor(color);
        this.setImages(images);
    }

    public FigureEntity(UUID id, String name, String shortDescription, String longDescription, Enum<?> subCategory, int price, int amount, String color, List<String> images, Date createdAt) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.category = subCategory.getClass().getSimpleName();
        this.subCategory = subCategory;
        this.price = price;
        this.amount = amount;
        this.color = color;
        this.images = images;
        this.createdAt = createdAt;
    }
}
