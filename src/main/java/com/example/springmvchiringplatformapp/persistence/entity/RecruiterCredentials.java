package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RecruiterCredentials extends Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "recruiter_id", unique = true, nullable = false)
    private Recruiter recruiter;

    public RecruiterCredentials(String username, String password, Recruiter recruiter) {
        super(username, password);
        this.recruiter = recruiter;
    }

    @Override
    public String toString() {
        return "RecruiterCredentials{" +
                "id=" + id +
                ", username=" + getUserId() +
                ", password=" + getPassword() +
                '}';
    }
}
