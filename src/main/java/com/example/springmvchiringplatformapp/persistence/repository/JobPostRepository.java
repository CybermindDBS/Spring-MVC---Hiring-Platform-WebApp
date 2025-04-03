package com.example.springmvchiringplatformapp.persistence.repository;

import com.example.springmvchiringplatformapp.persistence.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
}
