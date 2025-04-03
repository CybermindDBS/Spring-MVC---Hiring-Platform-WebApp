package com.example.springmvchiringplatformapp.persistence.repository;

import com.example.springmvchiringplatformapp.persistence.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JobApplicationPagingAndSortingRepository extends PagingAndSortingRepository<JobApplication, Long> {
    Page<JobApplication> findAllByJobPost_PostId(Long postId, Pageable pageable);

    int countAllByJobPost_PostId(Long postId);
}
