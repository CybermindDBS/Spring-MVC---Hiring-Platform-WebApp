package com.example.springmvchiringplatformapp.persistence.repository;

import com.example.springmvchiringplatformapp.persistence.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Credentials findByUserId(String userID);
}
