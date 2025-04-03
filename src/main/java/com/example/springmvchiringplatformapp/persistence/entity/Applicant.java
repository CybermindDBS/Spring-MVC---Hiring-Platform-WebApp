package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long applicantId;
    private String name;
    private String email;
    private String location;
    private Integer experience;
    @ElementCollection
    @CollectionTable(name = "applicant_skills", joinColumns = @JoinColumn(name = "applicant_id"))
    @Column(name = "skills")
    private List<String> skills = new ArrayList<>();
    @Lob
    private byte[] resume;
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "applicant", cascade = CascadeType.ALL)
    private ApplicantCredentials credentials;
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> jobApplications = new ArrayList<>();

    public Applicant() {
        this.credentials = new ApplicantCredentials();
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(String filePath) {
        try {
            Path path = Path.of(getClass().getClassLoader().getResource(filePath).toURI());
            this.resume = Files.readAllBytes(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            this.resume = null;
        }
    }

    public void setResume(byte[] bytes) {
        this.resume = bytes;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "applicantId=" + applicantId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", experience=" + experience +
                ", skills=" + skills +
                ", resume size=" + (resume == null ? null : resume.length) +
                ", credentials=" + credentials +
                ", jobApplications=" + jobApplications +
                '}';
    }
}
