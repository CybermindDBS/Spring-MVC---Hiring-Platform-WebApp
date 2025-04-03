package com.example.springmvchiringplatformapp.service;

import com.example.springmvchiringplatformapp.persistence.entity.ApplicantCredentials;
import com.example.springmvchiringplatformapp.persistence.entity.Credentials;
import com.example.springmvchiringplatformapp.persistence.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {

    private static final int USER_NOT_FOUND = -1;
    private static final int INCORRECT_PASSWORD = 0;
    private static final int APPLICANT = 1;
    private static final int RECRUITER = 2;
    private final CredentialsRepository repo;

    @Autowired
    public CredentialsService(CredentialsRepository repo) {
        this.repo = repo;
    }

    public Credentials findByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    public int authenticate(Credentials credentials) {
        Credentials persistentCredentials = findByUserId(credentials.getUserId());
        if (persistentCredentials == null) return USER_NOT_FOUND;
        else if (BCrypt.checkpw(credentials.getPassword(), persistentCredentials.getPassword())) {
            return persistentCredentials instanceof ApplicantCredentials ? APPLICANT : RECRUITER;
        } else return INCORRECT_PASSWORD;
    }
}
