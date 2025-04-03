package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String userId;
    private String password;

    public Credentials(String username, String password) {
        this.userId = username;
        this.password = password;
    }
}
