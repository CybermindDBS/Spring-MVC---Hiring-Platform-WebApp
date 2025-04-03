package com.example.springmvchiringplatformapp.service;

import com.example.springmvchiringplatformapp.persistence.entity.Recruiter;
import com.example.springmvchiringplatformapp.persistence.entity.RecruiterCredentials;
import com.example.springmvchiringplatformapp.persistence.repository.RecruiterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecruiterService {
    private final RecruiterRepository repo;

    @Autowired
    public RecruiterService(RecruiterRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void save(Recruiter recruiter) {
        repo.save(recruiter);
    }

    public List<Recruiter> findAll() {
        return repo.findAll();
    }

    public Recruiter findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    public Recruiter findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    public Boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Transactional
    public void update(Recruiter recruiter) {
        Recruiter persistentRecruiter = repo.findByEmail(recruiter.getEmail()).orElseThrow(() -> new EntityNotFoundException("Record not found"));

        if (recruiter.getName() != null)
            persistentRecruiter.setName(recruiter.getName());
//        if (recruiter.getEmail() != null)
//            persistentRecruiter.setEmail(recruiter.getEmail());
        if (recruiter.getLocation() != null)
            persistentRecruiter.setLocation(recruiter.getLocation());
        if (recruiter.getLogo() != null)
            persistentRecruiter.setLogo(recruiter.getLogo());
//        if (recruiter.getJobPosts() != null && !recruiter.getJobPosts().isEmpty())
//            if (!recruiter.getJobPosts().equals(persistentRecruiter.getJobPosts())) {
//                persistentRecruiter.getJobPosts().clear();
//                persistentRecruiter.getJobPosts().addAll(recruiter.getJobPosts());
//            }

        if (recruiter.getCredentials() != null) {
            RecruiterCredentials persistentRecruiterCredentials = persistentRecruiter.getCredentials();
//            if (recruiter.getCredentials().getUserId() != null)
//                persistentRecruiterCredentials.setUserId(recruiter.getCredentials().getUserId());
            if (recruiter.getCredentials().getPassword() != null)
                persistentRecruiterCredentials.setPassword(BCrypt.hashpw(recruiter.getCredentials().getPassword(), BCrypt.gensalt()));
        }

        repo.save(persistentRecruiter);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Record not found");
        repo.deleteById(id);
    }
}
