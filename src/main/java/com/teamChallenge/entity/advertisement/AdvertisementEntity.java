package com.teamChallenge.entity.advertisement;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "advertisements")
@Data
@NoArgsConstructor
public class AdvertisementEntity {

    @Id
    private String id;

    @Size(max = 150)
    @Column(nullable = false)
    private String text;

    @URL
    @Column(nullable = false)
    private String url;

    public AdvertisementEntity(String text, String url) {
        this.text = text;
        this.url = url;
    }
}
