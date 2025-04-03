package com.example.springmvchiringplatformapp.service;

import com.example.springmvchiringplatformapp.persistence.entity.Applicant;
import com.example.springmvchiringplatformapp.persistence.entity.ApplicantCredentials;
import com.example.springmvchiringplatformapp.persistence.repository.ApplicantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicantService {
    private final ApplicantRepository repo;

    @Autowired
    public ApplicantService(ApplicantRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void save(Applicant applicant) {
        repo.save(applicant);
    }

    public List<Applicant> findAll() {
        return repo.findAll();
    }

    public Applicant findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    public Applicant findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    public boolean existByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Transactional
    public void update(Applicant applicant) {
        Applicant persistentApplicant = repo.findByEmail(applicant.getEmail()).orElseThrow(() -> new EntityNotFoundException("Record not found > " + applicant));

        if (applicant.getName() != null)
            persistentApplicant.setName(applicant.getName());
//        if (applicant.getEmail() != null)
//            persistentApplicant.setEmail(applicant.getEmail());
        if (applicant.getLocation() != null)
            persistentApplicant.setLocation(applicant.getLocation());
        if (applicant.getExperience() != null)
            persistentApplicant.setExperience(applicant.getExperience());
        if (applicant.getResume() != null)
            persistentApplicant.setResume(applicant.getResume());
        if (applicant.getSkills() != null)
            persistentApplicant.setSkills(applicant.getSkills());
//        if (applicant.getJobApplications() != null && !applicant.getJobApplications().isEmpty())
//            if (!applicant.getJobApplications().equals(persistentApplicant.getJobApplications())) {
//                persistentApplicant.getJobApplications().clear();
//                persistentApplicant.getJobApplications().addAll(applicant.getJobApplications());
//            }

        if (applicant.getCredentials() != null) {
            ApplicantCredentials persistentApplicantCredentials = persistentApplicant.getCredentials();
//            if (applicant.getCredentials().getUserId() != null)
//                persistentApplicantCredentials.setUserId(applicant.getCredentials().getUserId());
            if (applicant.getCredentials().getPassword() != null)
                persistentApplicantCredentials.setPassword(BCrypt.hashpw(applicant.getCredentials().getPassword(), BCrypt.gensalt()));
        }

        repo.save(persistentApplicant);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Record not found");
        repo.deleteById(id);
    }
}
