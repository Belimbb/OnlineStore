package com.teamChallenge.Users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
public class UserEntity {

    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        role = Roles.USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name, email, password;

    @Column(nullable = false)
    private Roles role;

    @Column(nullable = false)
    @CreatedDate
    private Date createdAt;
}
