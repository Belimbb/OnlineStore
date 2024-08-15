package com.teamChallenge.entity.review.reply;

import com.teamChallenge.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    private String username;

    @Column(nullable = false)
    @CreatedDate
    private Date creationDate;

    @Size(max = 150)
    private String text;

    private int likes = 0;

    private int dislikes = 0;

    public Reply(String username, String text, int likes, int dislikes) {
        this.username = username;
        this.text = text;
        this.likes = likes;
        this.dislikes = dislikes;
        this.creationDate = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reply reply = (Reply) o;
        return Objects.equals(username, reply.username) && Objects.equals(text, reply.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, text);
    }
}