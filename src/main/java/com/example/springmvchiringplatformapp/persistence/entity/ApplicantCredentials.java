package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ApplicantCredentials extends Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "applicant_id", unique = true, nullable = false)
    private Applicant applicant;

    public ApplicantCredentials(String userid, String password, Applicant applicant) {
        super(userid, password);
        this.applicant = applicant;
    }

    @Override
    public String toString() {
        return "ApplicantCredentials{" +
                "id=" + id +
                ", username=" + getUserId() +
                ", password=" + getPassword() +
                '}';
    }
}
