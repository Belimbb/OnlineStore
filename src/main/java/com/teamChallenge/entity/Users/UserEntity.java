package com.teamChallenge.entity.Users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        role = Roles.USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username, email, password;

    @Column(nullable = false)
    private Roles role;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;
}
