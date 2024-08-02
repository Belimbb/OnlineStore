package com.teamChallenge.entity.banner;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String title, description, collectionName, imageName;

    public BannerEntity(String title, String description, String collectionName, String imageName) {
        this.title = title;
        this.description = description;
        this.collectionName = collectionName;
        this.imageName = imageName;
    }
}
