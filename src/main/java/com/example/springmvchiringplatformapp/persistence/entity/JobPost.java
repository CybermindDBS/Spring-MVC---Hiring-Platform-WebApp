package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postId;
    private String title;
    @Column(name = "desc", length = 5000)
    private String desc;
    private int minExp;
    private String location;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications = new ArrayList<>();

    @Override
    public String toString() {
        return "JobPost{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", minExp=" + minExp +
                ", location='" + location + '\'' +
                ", recruiter=" + recruiter.getName() +
                ", jobApplications=" + jobApplications +
                '}';
    }
}
