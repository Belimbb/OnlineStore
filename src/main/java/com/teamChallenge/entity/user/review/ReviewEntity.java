package com.teamChallenge.entity.user.review;

import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    private String id;

    @Column(nullable = false)
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 5, message = "Score must be at most 5")
    private byte score;

    @DBRef
    private UserEntity user;

    @Column(nullable = false)
    @CreatedDate
    private Date creationDate;

    @Column
    private String advantages;

    @Column
    private String disadvantages;

    @DBRef
    private FigureEntity figure;

    public ReviewEntity(byte score, UserEntity user, String advantages, String disadvantages, FigureEntity figure) {
        this.score = score;
        this.user = user;
        this.advantages = advantages;
        this.disadvantages = disadvantages;
        this.figure = figure;
    }
}
