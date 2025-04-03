package com.example.springmvchiringplatformapp.controller;

import com.example.springmvchiringplatformapp.persistence.entity.Applicant;
import com.example.springmvchiringplatformapp.persistence.entity.JobApplication;
import com.example.springmvchiringplatformapp.persistence.entity.JobPost;
import com.example.springmvchiringplatformapp.service.ApplicantService;
import com.example.springmvchiringplatformapp.service.JobApplicationService;
import com.example.springmvchiringplatformapp.service.JobPostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/applicant")
public class ApplicantController {
    private ApplicantService applicantService;
    private JobPostService jobPostService;
    private JobApplicationService jobApplicationService;

    @Autowired
    public void setApplicantService(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @Autowired
    public void setJobPostService(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @Autowired
    public void setJobApplicationService(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/profile")
    public String showApplicantProfile(Model model, HttpSession session) {
        Applicant applicant = applicantService.findByEmail((String) session.getAttribute("userId"));
        applicant.getCredentials().setPassword(null);
        model.addAttribute("applicant", applicant);
        return "applicant/profile";
    }

    @PostMapping("/update-profile")
    public String updateApplicantProfile(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Applicant applicant, @RequestParam MultipartFile resumeFile, HttpSession session) {
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                applicant.setResume(resumeFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        applicant.setEmail((String) session.getAttribute("userId"));
        applicantService.update(applicant);

        redirectAttributes.addFlashAttribute("msgProfile", "Your profile has been updated!");
        return "redirect:/applicant/profile";
    }

    @GetMapping("/apply-jobs")
    public String showJobApplicationPage(Model model, HttpSession session) {
        Map<String, List<JobPost>> jobPostsMap = jobPostService.retrieveAppliedAndOpenJobsByApplicantEmail((String) session.getAttribute("userId"));
        model.addAttribute("appliedJobPosts", jobPostsMap.get("appliedPosts"));
        model.addAttribute("notAppliedJobPosts", jobPostsMap.get("unAppliedPosts"));
        return "applicant/apply-jobs";
    }

    @PostMapping("/apply")
    public String applyJob(Model model, @RequestParam String postId, HttpSession session) {
        Applicant persistentApplicant = applicantService.findByEmail((String) session.getAttribute("userId"));
        JobPost persistentJobPost = jobPostService.findById(Long.parseLong(postId));
        jobApplicationService.save(new JobApplication(persistentJobPost, persistentApplicant));
        return "redirect:/applicant/apply-jobs";
    }

    @PostMapping("/search-jobs")
    public String searchJobs(Model model, @RequestParam String searchText, HttpSession session) {
        Map<String, List<JobPost>> jobPostsMap = jobPostService.retrieveAppliedAndOpenJobsByApplicantEmail((String) session.getAttribute("userId"), jobPostService.searchJobs(searchText));

        model.addAttribute("appliedJobPosts", jobPostsMap.get("appliedPosts"));
        model.addAttribute("notAppliedJobPosts", jobPostsMap.get("unAppliedPosts"));
        return "applicant/apply-jobs";
    }

    @GetMapping("/view-applications")
    public String showApplicationStatus(Model model, HttpSession session) {
        Map<String, List<JobApplication>> jobApplicationsMap = jobApplicationService.fetchClassifiedJobAplByApplicantEmail((String) session.getAttribute("userId"));

        model.addAttribute("shortlisted", jobApplicationsMap.get("shortlisted"));
        model.addAttribute("accepted", jobApplicationsMap.get("accepted"));
        model.addAttribute("applied", jobApplicationsMap.get("applied"));
        model.addAttribute("rejected", jobApplicationsMap.get("rejected"));
        return "applicant/view-applications";
    }

    @GetMapping("/accept-job-offer")
    public String acceptJobOffer(Model model, @RequestParam String jobAplId) {
        jobApplicationService.updateStatus(Long.parseLong(jobAplId), 2);
        return "redirect:/applicant/view-applications";
    }

    @GetMapping("/delete-application")
    public String deleteApplication(Model model, @RequestParam String jobAplId, HttpSession session) {
        jobApplicationService.deleteById(Long.parseLong(jobAplId));
        return "redirect:/applicant/view-applications";
    }
}
