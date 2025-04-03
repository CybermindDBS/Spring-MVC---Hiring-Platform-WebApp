package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private LocalDate appliedOn;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private JobPost jobPost;

    private Integer status = 0;

    public JobApplication(JobPost jobPost, Applicant applicant) {
        this.jobPost = jobPost;
        this.applicant = applicant;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "id=" + id +
                ", appliedOn=" + appliedOn +
                ", applicant=" + applicant.getName() +
                ", jobPost=" + jobPost.getTitle() +
                ", status=" + status +
                '}';
    }
}
