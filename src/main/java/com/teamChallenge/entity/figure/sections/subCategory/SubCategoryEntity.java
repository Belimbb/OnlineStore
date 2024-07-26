package com.teamChallenge.entity.figure.sections.subCategory;

import com.teamChallenge.entity.figure.sections.category.CategoryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sub_categories")
@Data
@AllArgsConstructor
public class SubCategoryEntity {

    @Id
    private String id;

    @Column(nullable = false, name = "sub_category_name")
    private String subCategoryName;

    @DBRef
    private CategoryEntity category;
}
