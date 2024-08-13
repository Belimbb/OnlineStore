package com.teamChallenge.entity.banner;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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

    public BannerEntity(String title, String description, String collectionName, String imageName) {
        setUp(title, description, collectionName, imageName);
    }

    public BannerEntity(String id, String title, String description, String collectionName, String imageName) {
        setUp(title, description, collectionName, imageName);
        this.id = id;
    }

    private void setUp(String title, String description, String collectionName, String imageName){
        this.title = title;
        this.description = description;
        this.collectionName = collectionName;
        this.imageName = imageName;
        this.uniqueHash = generateUniqueHash();
    }
}
