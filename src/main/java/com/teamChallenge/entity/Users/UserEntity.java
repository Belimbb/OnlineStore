package com.teamChallenge.entity.Users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        role = Roles.USER;
    }
}