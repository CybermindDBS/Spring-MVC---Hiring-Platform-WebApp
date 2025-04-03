package com.example.springmvchiringplatformapp.service;

import com.example.springmvchiringplatformapp.persistence.entity.JobApplication;
import com.example.springmvchiringplatformapp.persistence.repository.JobApplicationPagingAndSortingRepository;
import com.example.springmvchiringplatformapp.persistence.repository.JobApplicationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repo;
    private final JobApplicationPagingAndSortingRepository repo2;

    @Autowired
    public JobApplicationService(JobApplicationRepository repo, JobApplicationPagingAndSortingRepository repo2) {
        this.repo = repo;
        this.repo2 = repo2;
    }

    @Transactional
    public void save(JobApplication jobApplication) {
        repo.save(jobApplication);
    }

    public List<JobApplication> findAll() {
        return repo.findAll();
    }

    public List<JobApplication> findAllByEmail(String email) {
        return repo.findAllByApplicant_Email(email);
    }

    public JobApplication findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    public Map<String, List<JobApplication>> fetchClassifiedJobAplByApplicantEmail(String email) {
        Map<String, List<JobApplication>> jobApplicationMap = new HashMap<>();
        List<JobApplication> jobApplications = findAllByEmail(email);
        jobApplicationMap.put("accepted", jobApplications.stream().filter((appl) -> appl.getStatus() == 2).toList());
        jobApplicationMap.put("shortlisted", jobApplications.stream().filter((appl) -> appl.getStatus() == 1).toList());
        jobApplicationMap.put("applied", jobApplications.stream().filter((appl) -> appl.getStatus() == 0).toList());
        jobApplicationMap.put("rejected", jobApplications.stream().filter((appl) -> appl.getStatus() == -1).toList());
        return jobApplicationMap;
    }

    public Page<JobApplication> pageAndSortJobApplicationsByJobPostId(Long postId, int pageSize, int pageNo, String sortBy) {
        int maxPages, totalRecords;
        totalRecords = repo2.countAllByJobPost_PostId(postId);
        maxPages = (int) Math.ceil(totalRecords / (float) pageSize);

        Pageable pageable = PageRequest.of(Math.min(pageNo - 1, Math.max(maxPages - 1, 0)), pageSize, Sort.by(sortBy.equals("Applied on") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy.equals("Applied on") ? "appliedOn" : "applicant.experience"));
        return repo2.findAllByJobPost_PostId(postId, pageable);
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        JobApplication persistentJobApplication = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Record not found"));
        persistentJobApplication.setStatus(status);
        repo.save(persistentJobApplication);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Record not found");
        repo.deleteById(id);
    }
}
