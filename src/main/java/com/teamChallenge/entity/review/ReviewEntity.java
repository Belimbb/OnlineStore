package com.teamChallenge.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.review.reply.Reply;
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
import java.util.List;
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

    private String text;

    @Column
    private String advantages;

    @Column
    private String disadvantages;

    private List<String> videos;

    private List<String> photos;

    private List<Reply> replies;

    private int likes = 0;

    private int dislikes = 0;

    @DBRef
    @JsonBackReference
    private FigureEntity figure;

    public ReviewEntity(byte score, UserEntity user, String text, String advantages, String disadvantages,
                        List<String> videos, List<String> photos, FigureEntity figure) {
        this.score = score;
        this.user = user;
        this.text = text;
        this.advantages = advantages;
        this.disadvantages = disadvantages;
        this.videos = videos;
        this.photos = photos;
        this.figure = figure;
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "id='" + id + '\'' +
                ", score=" + score +
                ", userId=" + user.getId() +
                ", creationDate=" + creationDate +
                ", advantages='" + advantages + '\'' +
                ", disadvantages='" + disadvantages + '\'' +
                ", figureId=" + figure.getId() +
                '}';
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
