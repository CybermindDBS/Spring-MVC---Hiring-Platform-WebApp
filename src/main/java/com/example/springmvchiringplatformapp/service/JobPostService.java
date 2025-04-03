package com.example.springmvchiringplatformapp.service;

import com.example.springmvchiringplatformapp.persistence.entity.JobApplication;
import com.example.springmvchiringplatformapp.persistence.entity.JobPost;
import com.example.springmvchiringplatformapp.persistence.repository.JobPostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JobPostService {
    private final JobPostRepository repo;

    private JobApplicationService jobApplicationService;

    @Autowired
    public JobPostService(JobPostRepository repo) {
        this.repo = repo;
    }

    @Autowired
    public void setJobApplicationService(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Transactional
    public void save(JobPost jobPost) {
        repo.save(jobPost);
    }

    public List<JobPost> findAll() {
        return repo.findAll();
    }

    public JobPost findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    @Transactional
    public void update(JobPost jobPost) {
        JobPost persistentJobPost = repo.findById(jobPost.getPostId()).orElseThrow(() -> new EntityNotFoundException("Record not found"));
        if (jobPost.getTitle() != null)
            persistentJobPost.setTitle(jobPost.getTitle());
        if (jobPost.getDesc() != null)
            persistentJobPost.setDesc(jobPost.getDesc());
        if (jobPost.getLocation() != null)
            persistentJobPost.setLocation(jobPost.getLocation());
        if (jobPost.getMinExp() != 0)
            persistentJobPost.setMinExp(jobPost.getMinExp());
        repo.save(persistentJobPost);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Record not found");
        repo.deleteById(id);
    }

    public long getTotalCount() {
        return repo.count();
    }

    public Map<String, List<JobPost>> retrieveAppliedAndOpenJobsByApplicantEmail(String email) {
        return retrieveAppliedAndOpenJobsByApplicantEmail(email, findAll());
    }

    public Map<String, List<JobPost>> retrieveAppliedAndOpenJobsByApplicantEmail(String email, List<JobPost> jobPosts) {
        Map<String, List<JobPost>> jobPostsMap = new HashMap<>();
        List<JobPost> appliedJobPosts = new ArrayList<>();
        List<JobPost> openJobPosts = new ArrayList<>();

        HashSet<Long> appliedJobPostIds = jobApplicationService.findAllByEmail(email).stream().map((jobApplication -> jobApplication.getJobPost().getPostId())).collect(Collectors.toCollection(HashSet::new));

        for (JobPost jobPost : jobPosts) {
            if (appliedJobPostIds.contains(jobPost.getPostId()))
                appliedJobPosts.add(jobPost);
            else openJobPosts.add(jobPost);
        }

        jobPostsMap.put("appliedPosts", appliedJobPosts);
        jobPostsMap.put("unAppliedPosts", openJobPosts);
        return jobPostsMap;
    }

    public List<JobPost> searchJobs(String searchText) {
        List<JobPost> jobPosts = findAll();
        if (searchText.isEmpty()) return jobPosts;

        HashSet<String> keywords = Arrays.stream(searchText.toLowerCase().split("[ ,.]+")).collect(Collectors.toCollection(HashSet::new));

        return jobPosts.stream().filter(post -> {
            HashSet<String> words = Stream.of(post.getRecruiter().getName(), post.getTitle(), post.getDesc(), post.getLocation()).flatMap(text -> Arrays.stream(text.split("[ ,.]+"))).map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));
            return words.stream().anyMatch(keywords::contains);
        }).toList();
    }

    public String fetchAcceptedCandidateDetails(String postId) {
        List<JobApplication> acceptedApplications = findById(Long.parseLong(postId)).getJobApplications().stream().filter(jobApplication -> jobApplication.getStatus() == 2).toList();
        StringBuilder sb = new StringBuilder();
        acceptedApplications.forEach((apl) -> sb.append(Stream.of(apl.getApplicant().getName(), apl.getApplicant().getEmail(), apl.getApplicant().getLocation(), String.valueOf(apl.getApplicant().getExperience()), "[" + String.join(",", apl.getApplicant().getSkills()) + "]", String.valueOf(apl.getAppliedOn()))
                .reduce("", (first, second) -> first + ", " + second).substring(2)).append("\n"));
        return sb.toString();
    }
}
