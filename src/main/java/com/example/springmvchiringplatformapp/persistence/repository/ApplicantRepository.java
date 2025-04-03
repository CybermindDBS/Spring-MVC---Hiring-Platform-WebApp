package com.example.springmvchiringplatformapp.persistence.repository;

import com.example.springmvchiringplatformapp.persistence.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Optional<Applicant> findByEmail(String email);

    Boolean existsByEmail(String email);
}
