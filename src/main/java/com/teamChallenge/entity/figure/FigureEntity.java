package com.teamChallenge.entity.figure;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamChallenge.entity.figure.additionalInfo.AdditionalInfo;
import com.teamChallenge.entity.figure.sections.Labels;

import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import com.teamChallenge.entity.figure.sections.subCategory.SubCategoryEntity;
import com.teamChallenge.entity.review.ReviewEntity;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private AdditionalInfo additionalInfo;

    @Column
    private int purchaseCount;

    @DBRef
    @JsonManagedReference
    private List<ReviewEntity> reviews;

    @Column
    private double averageRating;

    @Column
    private Map<Byte, Integer> ratingDistribution;

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

    private void updateRating(){
        this.averageRating = reviews.stream()
                .mapToInt(ReviewEntity::getScore)
                .average()
                .orElse(0.0);

        this.ratingDistribution = reviews.stream()
                .collect(Collectors.groupingBy(
                        ReviewEntity::getScore,
                        Collectors.reducing(0, e -> 1, Integer::sum)
                ));
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;

        updateRating();
    }

    private String generateUniqueHash() {
        String data = name + shortDescription + longDescription + category.getName() + subCategory.getName() + currentPrice + oldPrice + amount;
        return DigestUtils.sha256Hex(data);
    }

    public FigureEntity(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                        Labels label, int currentPrice, int oldPrice, int amount, List<String> images, AdditionalInfo additionalInfo) {
        setup(name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount, images, additionalInfo);
    }

    public FigureEntity(String id, String name, String shortDescription, String longDescription,
                        SubCategoryEntity subCategory, Labels label, int currentPrice, int oldPrice,
                        int amount, List<String> images, AdditionalInfo additionalInfo,
                        List<ReviewEntity> reviews, Date createdAt) {
        setup(name, shortDescription, longDescription, subCategory, label, currentPrice, oldPrice,
                amount, images, additionalInfo);
        this.setId(id);
        this.setReviews(reviews);
        this.setCreatedAt(createdAt);

        updateRating();
    }

    private void setup(String name, String shortDescription, String longDescription, SubCategoryEntity subCategory,
                       Labels label, int currentPrice, int oldPrice, int amount, List<String> images, AdditionalInfo additionalInfo){
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
        this.setAdditionalInfo(additionalInfo);
        this.setUniqueHash(generateUniqueHash());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FigureEntity entity = (FigureEntity) o;
        return Objects.equals(uniqueHash, entity.uniqueHash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueHash);
    }
}