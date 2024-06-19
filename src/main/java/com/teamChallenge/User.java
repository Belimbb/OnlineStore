package com.teamChallenge;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Entity
@Data
public class User   {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    HashMap<String, Double> df;
}
