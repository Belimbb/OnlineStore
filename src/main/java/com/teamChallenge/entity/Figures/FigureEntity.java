package com.teamChallenge.entity.Figures;

import com.teamChallenge.entity.Products.Product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "figures")
@Data
@NoArgsConstructor
public class FigureEntity extends Product {
    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;

    public FigureEntity(String name, String shortDescription, String longDescription, Enum category, int price, int amount, String color, List<String> images) {
        this.setName(name);
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setCategory(category);
        this.setPrice(price);
        this.setAmount(amount);
        this.setColor(color);
        this.setImages(images);
    }

    public FigureEntity(UUID id, String name, String shortDescription, String longDescription, Enum category, int price, int amount, String color, List<String> images, Date createdAt) {
        this.setId(id);
        this.setName(name);
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setCategory(category);
        this.setPrice(price);
        this.setAmount(amount);
        this.setColor(color);
        this.setImages(images);
        this.setCreatedAt(createdAt);
    }
}
