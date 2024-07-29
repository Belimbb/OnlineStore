package com.teamChallenge.entity.figure;

import com.teamChallenge.entity.figure.sections.Labels;

import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
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

    @Column
    private Boolean inWishList = false;

    @Column (nullable = false)
    private int currentPrice;

    @Column (nullable = false)
    private int oldPrice;

    @Column (nullable = false)
    private int amount;

    @Column(nullable = false)
    private String color;

    @Column
    private List<String> images;

    @Column
    private int purchaseCount;

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
        String data = name + shortDescription + longDescription + category.getName() + subCategory.getName() + currentPrice + oldPrice + amount + color;
        return DigestUtils.sha256Hex(data);
    }

    public FigureEntity(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                        Labels label, Boolean inWishList, int currentPrice, int oldPrice, int amount, String color, List<String> images) {
        setup(name, shortDescription, longDescription, subCategory, label, inWishList, currentPrice, oldPrice, amount, color, images);
    }

    public FigureEntity(String id, String name, String shortDescription, String longDescription,
                        SubCategoryEntity subCategory, Labels label, Boolean inWishList, int currentPrice, int oldPrice,
                        int amount, String color, List<String> images, Date createdAt) {
        setup(name, shortDescription, longDescription, subCategory, label, inWishList, currentPrice, oldPrice, amount, color, images);
        this.setId(id);
        this.setCreatedAt(createdAt);
    }

    private void setup(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory, Labels label,Boolean inWishList, int currentPrice, int oldPrice, int amount, String color, List<String> images){
        this.setName(name);
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setCategory(subCategory.getCategory());
        this.setSubCategory(subCategory);
        this.setLabel(label);
        this.setInWishList(inWishList);
        this.setCurrentPrice(currentPrice);
        this.setOldPrice(oldPrice);
        this.setAmount(amount);
        this.setColor(color);
        this.setImages(images);
        this.setUniqueHash(generateUniqueHash());
    }
}