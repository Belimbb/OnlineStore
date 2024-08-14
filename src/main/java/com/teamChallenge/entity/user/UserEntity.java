package com.teamChallenge.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teamChallenge.entity.figure.FigureEntity;
import com.teamChallenge.entity.order.OrderEntity;
import com.teamChallenge.entity.address.AddressInfo;
import com.teamChallenge.entity.review.ReviewEntity;
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

import java.util.*;

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

    @Column(unique = true)
    private String phoneNumber;

    @NotNull
    @Size(min = 8, max = 100)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Roles role;

    private AddressInfo addressInfo;

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

    @DBRef
    private List<OrderEntity> orderHistory;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Column(nullable = false)
    private boolean isPasswordVerified;

    @Column
    private UUID emailVerificationCode, passwordVerificationCode;

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        role = Roles.USER;
        recentlyViewed = new ArrayList<>();
        isEmailVerified = false;
        isPasswordVerified = false;
        emailVerificationCode = UUID.randomUUID();
        passwordVerificationCode = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password, role);
    }
}
