package com.teamChallenge.entity.Figures;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "figures")
@Data
@NoArgsConstructor
public class FigureEntity {

    @Id
    private String id;

    @Size(max = 50)
    @Column (nullable = false)
    private String name;

    @Size(min = 10, max = 200)
    @Column (nullable = false)
    private String shortDescription;

    @Size(min = 10, max = 1000)
    @Column (nullable = false)
    private String longDescription;

    @Column (nullable = false)
    private Enum category;

    @Column (nullable = false)
    int price;

    @Column (nullable = false)
    int amount;

    @Column(nullable = false)
    private String color;

    @Column
    List<String> images;

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

    public FigureEntity(String id, String name, String shortDescription, String longDescription, Enum category, int price, int amount, String color, List<String> images, Date createdAt) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.category = category;
        this.price = price;
        this.amount = amount;
        this.color = color;
        this.images = images;
        this.createdAt = createdAt;
    }
}
