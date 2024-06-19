package com.teamChallenge.Figures;

import com.teamChallenge.Products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "figures")
@Getter
@Setter
@NoArgsConstructor
public class FigureEntity extends Product {

    public FigureEntity(String name, String description, int price, int amount, String color, List<String> images) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setAmount(amount);
        this.color = color;
        this.setImages(images);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;
}
