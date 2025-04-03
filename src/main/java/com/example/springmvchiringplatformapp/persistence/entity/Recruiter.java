package com.example.springmvchiringplatformapp.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recruiterId;
    private String name;
    private String email;
    private String location;

    @Lob
    private byte[] logo;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private RecruiterCredentials credentials;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPost> jobPosts = new ArrayList<>();


    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(String filePath) {
        try {
            Path path = Path.of(getClass().getClassLoader().getResource(filePath).toURI());
            this.logo = Files.readAllBytes(path);
        } catch (IOException e) {
            this.logo = null;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLogo(byte[] bytes) {
        this.logo = bytes;
    }

    @Override
    public String toString() {
        return "Recruiter{" +
                "recruiterId=" + recruiterId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", logo size=" + logo.length +
                ", credentials=" + credentials +
                ", jobPosts=" + jobPosts +
                '}';
    }
}
