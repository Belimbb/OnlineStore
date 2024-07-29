package com.teamChallenge.entity.figure.sections.category;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    private String id;

    @Column(nullable = false, name = "category_name", unique = true)
    private String name;

    public CategoryEntity(String name) {
        this.name = name;
    }
}
