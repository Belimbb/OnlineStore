package com.teamChallenge.entity.figure;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamChallenge.entity.figure.sections.Labels;

import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.entity.user.review.ReviewEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    @DBRef
    private CategoryEntity category;

    @DBRef
    private SubCategoryEntity subCategory;

    @Column
    private Labels label;

    @Column (nullable = false)
    private int currentPrice;

    @Column (nullable = false)
    private int oldPrice;

    @Column (nullable = false)
    private int amount;

    @Column
    private List<String> images;

    @Column (nullable = false)
    private String theme, material, characterName, productType, typeOfFigure, country, packageSize, toySize;

    @Column
    private int purchaseCount;

    @DBRef
    @JsonManagedReference
    private List<ReviewEntity> reviews;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;

    @Indexed(unique = true)
    private String uniqueHash;

    @PrePersist
    public void prePersist() {
        if (uniqueHash==null||uniqueHash.isBlank()){
            this.uniqueHash = generateUniqueHash();
        }
    }

    private String generateUniqueHash() {
        String data = name + shortDescription + longDescription + category.getName() + subCategory.getName() + currentPrice + oldPrice + amount;
        return DigestUtils.sha256Hex(data);
    }

    public FigureEntity(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                        Labels label, int currentPrice, int oldPrice, int amount, List<String> images,
                        String theme, String material, String characterName, String productType, String typeOfFigure,
                        String country, String packageSize, String toySize) {
        setup(name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount, images, theme, material, characterName, productType, typeOfFigure, country, packageSize, toySize);
    }

    public FigureEntity(String id, String name, String shortDescription, String longDescription,
                        SubCategoryEntity subCategory, Labels label, int currentPrice, int oldPrice,
                        int amount, List<String> images,
                        String theme, String material, String characterName, String productType, String typeOfFigure,
                        String country, String packageSize, String toySize,
                        List<ReviewEntity> reviews, Date createdAt) {
        setup(name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount, images, theme, material, characterName, productType, typeOfFigure, country, packageSize, toySize);
        this.setId(id);
        this.setReviews(reviews);
        this.setCreatedAt(createdAt);
    }

    private void setup(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                       Labels label, int currentPrice, int oldPrice, int amount, List<String> images,
                       String theme, String material, String characterName, String productType, String typeOfFigure,
                       String country, String packageSize, String toySize){
        this.setName(name);
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setCategory(subCategory.getCategory());
        this.setSubCategory(subCategory);
        this.setLabel(label);
        this.setCurrentPrice(currentPrice);
        this.setOldPrice(oldPrice);
        this.setAmount(amount);
        this.setImages(images);
        this.setTheme(theme);
        this.setMaterial(material);
        this.setCharacterName(characterName);
        this.setProductType(productType);
        this.setTypeOfFigure(typeOfFigure);
        this.setCountry(country);
        this.setPackageSize(packageSize);
        this.setToySize(toySize);
        this.setUniqueHash(generateUniqueHash());
    }
}