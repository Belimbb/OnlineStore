package com.teamChallenge.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.user.review.ReviewEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Roles role;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;

    @DBRef
    @JsonManagedReference
    private List<ReviewEntity> reviews;

    @Column
    private List<FigureEntity> whishList;

    @DBRef
    private List<FigureEntity> recentlyViewed;

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        role = Roles.USER;
        recentlyViewed = new ArrayList<>();
    }
}
