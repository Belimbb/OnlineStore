package com.teamChallenge.entity.Figures;

import com.teamChallenge.entity.Products.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "figures")
@Data
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
