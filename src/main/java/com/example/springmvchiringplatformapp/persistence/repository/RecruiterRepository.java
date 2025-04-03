package com.example.springmvchiringplatformapp.persistence.repository;

import com.example.springmvchiringplatformapp.persistence.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByEmail(String email);

    Boolean existsByEmail(String email);
}
