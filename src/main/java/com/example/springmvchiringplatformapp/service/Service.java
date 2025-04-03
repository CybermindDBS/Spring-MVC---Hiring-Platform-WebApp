package com.example.springmvchiringplatformapp.service;

import com.example.springmvchiringplatformapp.persistence.entity.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class Service {
    @Autowired
    ApplicantService applicantService;
    @Autowired
    RecruiterService recruiterService;
    @Autowired
    JobPostService jobPostService;
    @Autowired
    JobApplicationService jobApplicationService;

    @Autowired
    Logger logger;

    public void setup() {
        Applicant applicant = new Applicant();
        applicant.setName("Don");
        applicant.setEmail("don@gmail.com");
        applicant.setLocation("Chennai");
        applicant.setExperience(6);
        applicant.setSkills(new ArrayList<>(List.of("Java", "JEE", "Hibernate", "Spring", "React")));
        applicant.setResume("media/resume-1.pdf");
        applicant.setCredentials(new ApplicantCredentials("don@gmail.com", BCrypt.hashpw("pass123", BCrypt.gensalt()), applicant));
        applicant.setJobApplications(new ArrayList<>());
        applicantService.save(applicant);

        Applicant applicant2 = new Applicant();
        applicant2.setName("Aron");
        applicant2.setEmail("aron@gmail.com");
        applicant2.setLocation("Bangalore");
        applicant2.setExperience(9);
        applicant2.setSkills(new ArrayList<>(List.of("Javascript", "Angular", "Next.js", "Typescript", "React")));
        applicant2.setResume("media/resume-1.pdf");
        applicant2.setCredentials(new ApplicantCredentials("aron@gmail.com", BCrypt.hashpw("pass123", BCrypt.gensalt()), applicant2));
        applicant2.setJobApplications(new ArrayList<>());
        applicantService.save(applicant2);

        Recruiter recruiter = new Recruiter();
        recruiter.setName("Quantum Works");
        recruiter.setEmail("talent@qw.com");
        recruiter.setLocation("Chennai");
        recruiter.setLogo("media/logo-1.jpg");
        recruiter.setCredentials(new RecruiterCredentials("talent@qw.com", BCrypt.hashpw("pass123", BCrypt.gensalt()), recruiter));
        recruiter.setJobPosts(new ArrayList<>());
        recruiterService.save(recruiter);

        Recruiter recruiter2 = new Recruiter();
        recruiter2.setName("Async");
        recruiter2.setEmail("hr@async.com");
        recruiter2.setLocation("Bangalore");
        recruiter2.setLogo("media/logo-1.jpg");
        recruiter2.setCredentials(new RecruiterCredentials("hr@async.com", BCrypt.hashpw("pass123", BCrypt.gensalt()), recruiter2));
        recruiter2.setJobPosts(new ArrayList<>());
        recruiterService.save(recruiter2);

        JobPost jobPost = new JobPost();
        jobPost.setRecruiter(recruiter);
        jobPost.setTitle("Full stack developer");
        jobPost.setDesc("Join our agile team to develop and maintain responsive web applications. You'll build dynamic user interfaces with HTML, CSS, and JavaScript, and develop robust back-end services using Java (Spring Boot). Collaborate with cross-functional teams to create and integrate RESTful APIs and manage databases.");
        jobPost.setLocation("Chennai");
        jobPost.setMinExp(4);
        jobPost.setJobApplications(new ArrayList<>());
        jobPostService.save(jobPost);

        JobPost jobPost2 = new JobPost();
        jobPost2.setRecruiter(recruiter);
        jobPost2.setTitle("Backend developer");
        jobPost2.setDesc("We are looking for a Backend Developer to design and build robust server-side applications. You will develop REST APIs, manage databases, and integrate with front-end systems to deliver scalable solutions.");
        jobPost2.setLocation("Chennai");
        jobPost2.setMinExp(3);
        jobPost2.setJobApplications(new ArrayList<>());
        jobPostService.save(jobPost2);

        JobPost jobPost3 = new JobPost();
        jobPost3.setRecruiter(recruiter2);
        jobPost3.setTitle("Frontend developer");
        jobPost3.setDesc("We are looking for a skilled Frontend Developer to join our team and help build responsive, high-performance web applications. You will work closely with designers and backend developers to deliver seamless user experiences.");
        jobPost3.setLocation("Bangalore");
        jobPost3.setMinExp(3);
        jobPost3.setJobApplications(new ArrayList<>());
        jobPostService.save(jobPost3);

        JobPost jobPost4 = new JobPost();
        jobPost4.setRecruiter(recruiter2);
        jobPost4.setTitle("UI/UX Designer");
        jobPost4.setDesc("Design intuitive, visually appealing, and user-friendly digital experiences. Collaborate with product managers, developers, and stakeholders to create interfaces that enhance engagement and usability.");
        jobPost4.setLocation("Remote");
        jobPost4.setMinExp(2);
        jobPost4.setJobApplications(new ArrayList<>());
        jobPostService.save(jobPost4);

        JobApplication jobApplication = new JobApplication(jobPost, applicant);
        jobApplicationService.save(jobApplication);

        JobApplication jobApplication2 = new JobApplication(jobPost4, applicant);
        jobApplicationService.save(jobApplication2);

        JobApplication jobApplication3 = new JobApplication(jobPost, applicant2);
        jobApplicationService.save(jobApplication3);
    }
}
