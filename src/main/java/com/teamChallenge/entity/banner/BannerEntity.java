package com.teamChallenge.entity.banner;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Column(nullable = false)
    @Size(min = 7, max = 7)
    private String titleColor, descriptionColor, collectionNameColor;

    @Indexed(unique = true)
    private String uniqueHash;

    @PrePersist
    public void prePersist() {
        if (uniqueHash==null||uniqueHash.isBlank()){
            this.uniqueHash = generateUniqueHash();
        }
    }

    private String generateUniqueHash() {
        String data = title+description+collectionName+imageName;
        return DigestUtils.sha256Hex(data);
    }

    public BannerEntity(String title, String description, String collectionName, String imageName,
                        String titleColor, String descriptionColor, String collectionNameColor) {
        setUp(title, description, collectionName, imageName, titleColor, descriptionColor, collectionNameColor);
    }

    public BannerEntity(String id, String title, String description, String collectionName, String imageName,
                        String titleColor, String descriptionColor, String collectionNameColor) {
        setUp(title, description, collectionName, imageName, titleColor, descriptionColor, collectionNameColor);
        this.id = id;
    }

    private void setUp(String title, String description, String collectionName, String imageName,
                       String titleColor, String descriptionColor, String collectionNameColor){
        this.title = title;
        this.description = description;
        this.collectionName = collectionName;
        this.imageName = imageName;
        this.titleColor = titleColor;
        this.descriptionColor = descriptionColor;
        this.collectionNameColor = collectionNameColor;
        this.uniqueHash = generateUniqueHash();
    }
}
