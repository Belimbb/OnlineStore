package com.teamChallenge;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Role {
    private int id;
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
