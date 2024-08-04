package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Labels;

import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.entity.user.review.ReviewEntity;
import jakarta.persistence.*;
import org.apache.commons.codec.digest.DigestUtils;

import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Column
    private Map<String, String> basicCharacteristics;

    @Column
    private Map<String, String> dimensions;

    @Column
    private int purchaseCount;

    @DBRef
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

    public void setBasicCharacteristic(String theme, String material, String characterName,
                                       String productType, String typeOfFigure, String country){
        basicCharacteristics = Map.of("Theme", theme, "Material", material, "Character name", characterName,
                "Product type", productType, "Type of figure", typeOfFigure, "Country", country);
    }

    public void setDimensions(String packageSize, String toySize){
        dimensions = Map.of("Package size", packageSize, "Toy size", toySize);
    }

    public FigureEntity(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                        Labels label, int currentPrice, int oldPrice, int amount,
                        Map<String, String> basicCharacteristics, Map<String, String> dimensions, List<String> images) {
        setup(name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount, basicCharacteristics, dimensions, images);
    }

    public FigureEntity(String id, String name, String shortDescription, String longDescription,
                        SubCategoryEntity subCategory, Labels label, int currentPrice, int oldPrice,
                        int amount, Map<String, String> basicCharacteristics,
                        Map<String, String> dimensions, List<String> images,
                        List<ReviewEntity> reviews, Date createdAt) {
        setup(name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount, basicCharacteristics, dimensions, images);
        this.setId(id);
        this.setReviews(reviews);
        this.setCreatedAt(createdAt);
    }

    private void setup(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                       Labels label, int currentPrice, int oldPrice, int amount,
                       Map<String, String> basicCharacteristics, Map<String, String> dimensions, List<String> images){
        this.setName(name);
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setCategory(subCategory.getCategory());
        this.setSubCategory(subCategory);
        this.setLabel(label);
        this.setCurrentPrice(currentPrice);
        this.setOldPrice(oldPrice);
        this.setAmount(amount);
        this.basicCharacteristics = basicCharacteristics;
        this.dimensions = dimensions;
        this.setImages(images);
        this.setUniqueHash(generateUniqueHash());
    }
}