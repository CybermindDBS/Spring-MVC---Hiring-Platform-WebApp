package com.example.springmvchiringplatformapp.controller;

import com.example.springmvchiringplatformapp.persistence.entity.JobApplication;
import com.example.springmvchiringplatformapp.persistence.entity.JobPost;
import com.example.springmvchiringplatformapp.persistence.entity.Recruiter;
import com.example.springmvchiringplatformapp.service.ApplicantService;
import com.example.springmvchiringplatformapp.service.JobApplicationService;
import com.example.springmvchiringplatformapp.service.JobPostService;
import com.example.springmvchiringplatformapp.service.RecruiterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {
    private RecruiterService recruiterService;
    private ApplicantService applicantService;
    private JobPostService jobPostService;
    private JobApplicationService jobApplicationService;

    @Autowired
    public void setJobPostService(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @Autowired
    public void setJobApplicationService(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Autowired
    public void setApplicantService(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @Autowired
    public void setRecruiterService(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping("/profile")
    public String showRecruiterProfile(Model model, HttpSession session) {
        Recruiter recruiter = recruiterService.findByEmail((String) session.getAttribute("userId"));
        recruiter.getCredentials().setPassword(null);
        model.addAttribute("recruiter", recruiter);
        return "recruiter/profile";
    }

    @PostMapping("/update-profile")
    public String updateRecruiterProfile(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Recruiter recruiter, @RequestParam MultipartFile logoFile, HttpSession session) {
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                recruiter.setLogo(logoFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        recruiter.setEmail((String) session.getAttribute("userId"));
        recruiterService.update(recruiter);
        redirectAttributes.addFlashAttribute("msgProfile", "Your profile has been updated!");
        return "redirect:/recruiter/profile";
    }

    @GetMapping("/campaigns")
    public String showRecruiterPanel(Model model, HttpSession session) {
        session.setAttribute("jobPosts", recruiterService.findByEmail((String) session.getAttribute("userId")).getJobPosts());
        session.removeAttribute("pageSize");
        session.removeAttribute("pageNo");
        session.removeAttribute("sortBy");
        session.removeAttribute("maxPageNo");
        return "recruiter/campaigns";
    }

    @GetMapping("/campaigns/edit")
    public String editCampaign(Model model, @RequestParam String postId) {
        JobPost jobPostEdit = jobPostService.findById(Long.parseLong(postId));
        model.addAttribute("jobPostEdit", jobPostEdit);
        model.addAttribute("showEditCampaign", true);
        return "recruiter/campaigns";
    }

    @PostMapping("/campaigns/edit-campaign")
    public String editCampaign(Model model, @ModelAttribute JobPost jobPostEdit) {
        jobPostService.update(jobPostEdit);
        return "redirect:/recruiter/campaigns";
    }

    @GetMapping("/campaigns/create")
    public String createCampaign(Model model) {
        JobPost jobPostCreate = new JobPost();
        model.addAttribute("jobPostCreate", jobPostCreate);
        model.addAttribute("showCreateCampaign", true);
        return "recruiter/campaigns";
    }

    @PostMapping("/campaigns/create-campaign")
    public String createCampaign(Model model, @ModelAttribute JobPost jobPostCreate, HttpSession session) {
        jobPostCreate.setRecruiter(recruiterService.findByEmail((String) session.getAttribute("userId")));
        jobPostService.save(jobPostCreate);
        return "redirect:/recruiter/campaigns";
    }

    @GetMapping("/campaigns/delete")
    public String deleteCampaign(Model model, @RequestParam String postId) {
        jobPostService.deleteById(Long.parseLong(postId));
        return "redirect:/recruiter/campaigns";
    }

    @GetMapping("/campaigns/view-applications")
    public String viewApplications(Model model, HttpSession session, @RequestParam String postId, @RequestParam(required = false) String pageSize, @RequestParam(required = false) String pageNo, @RequestParam(required = false) String sortBy) {
        model.addAttribute("showApplications", true);
        model.addAttribute("jobPostId", postId);

        if (pageSize != null && pageNo != null & sortBy != null || session.getAttribute("pageSize") != null && session.getAttribute("pageNo") != null && session.getAttribute("sortBy") != null) {
            Page<JobApplication> page;
            if (pageSize != null && pageNo != null & sortBy != null) {
                page = jobApplicationService.pageAndSortJobApplicationsByJobPostId(Long.parseLong(postId), Integer.parseInt(pageSize), Integer.parseInt(pageNo), sortBy);
                session.setAttribute("pageSize", pageSize);
                session.setAttribute("pageNo", page.getNumber() + 1);
                session.setAttribute("sortBy", sortBy);
                session.setAttribute("maxPageNo", page.getTotalPages());
            } else
                page = jobApplicationService.pageAndSortJobApplicationsByJobPostId(Long.parseLong(postId), Integer.parseInt((String) session.getAttribute("pageSize")), (int) session.getAttribute("pageNo"), (String) session.getAttribute("sortBy"));
            model.addAttribute("jobApplications", page.getContent());

        } else
            model.addAttribute("jobApplications", jobPostService.findById(Long.parseLong(postId)).getJobApplications());
        return "recruiter/campaigns";
    }

    @GetMapping("/campaigns/shortlist-candidate")
    public String shortlistCandidate(Model model, @RequestParam String applicationId, @RequestParam String postId) {
        jobApplicationService.updateStatus(Long.parseLong(applicationId), 1);
        return "redirect:/recruiter/campaigns/view-applications?postId=" + postId;
    }

    @GetMapping("/campaigns/reject-candidate")
    public String rejectCandidate(Model model, @RequestParam String applicationId, @RequestParam String postId) {
        jobApplicationService.updateStatus(Long.parseLong(applicationId), -1);
        return "redirect:/recruiter/campaigns/view-applications?postId=" + postId;
    }

    @GetMapping("/view-applicant")
    public String showViewApplicant(Model model, @RequestParam String applicantId) {
        model.addAttribute("applicant", applicantService.findById(Long.parseLong(applicantId)));
        return "recruiter/view-applicant";
    }

    @GetMapping("/campaigns/{postId}/download-accepted-candidate-details")
    public ResponseEntity<byte[]> downloadAcceptedCandidateDetails(Model model, @PathVariable String postId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "CandidateDetails-" + postId + ".txt\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        byte[] fileBytes = jobPostService.fetchAcceptedCandidateDetails(postId).getBytes(StandardCharsets.UTF_8);

        return new ResponseEntity<byte[]>(fileBytes, headers, HttpStatus.OK);
    }
}
