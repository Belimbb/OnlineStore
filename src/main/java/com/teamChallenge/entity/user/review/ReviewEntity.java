package com.teamChallenge.entity.user.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.util.Objects;

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
    @JsonBackReference
    private UserEntity user;

    @Column(nullable = false)
    @CreatedDate
    private Date creationDate;

    @Column
    private String advantages;

    @Column
    private String disadvantages;

    @DBRef
    @JsonBackReference
    private FigureEntity figure;

    public ReviewEntity(byte score, UserEntity user, String advantages, String disadvantages, FigureEntity figure) {
        this.score = score;
        this.user = user;
        this.advantages = advantages;
        this.disadvantages = disadvantages;
        this.figure = figure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntity that = (ReviewEntity) o;
        return score == that.score && Objects.equals(creationDate, that.creationDate) && Objects.equals(advantages, that.advantages) && Objects.equals(disadvantages, that.disadvantages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, creationDate, advantages, disadvantages);
    }
}
