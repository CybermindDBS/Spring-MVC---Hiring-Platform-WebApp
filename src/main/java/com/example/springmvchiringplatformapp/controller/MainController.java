package com.example.springmvchiringplatformapp.controller;

import com.example.springmvchiringplatformapp.persistence.entity.*;
import com.example.springmvchiringplatformapp.service.ApplicantService;
import com.example.springmvchiringplatformapp.service.CredentialsService;
import com.example.springmvchiringplatformapp.service.JobPostService;
import com.example.springmvchiringplatformapp.service.RecruiterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class MainController {

    private ApplicantService applicantService;
    private RecruiterService recruiterService;
    private CredentialsService credentialsService;
    private JobPostService jobPostService;


    @Autowired
    public void setCredentialsService(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Autowired
    public void setApplicantService(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @Autowired
    public void setRecruiterService(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @Autowired
    public void setJobPostService(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String showHome(Model model, HttpServletRequest request, @CookieValue(value = "accountType", defaultValue = "none") String accountType) {
        HttpSession session = request.getSession(false);
        if (session == null || accountType.equals("none")) {
            return "home";
        } else {
            if (accountType.equals("applicant")) {
                if (session.getAttribute("name") == null)
                    session.setAttribute("name", applicantService.findByEmail((String) session.getAttribute("userId")).getName());
                model.addAttribute("jobCount", jobPostService.getTotalCount());
                return "applicant/home";
            } else if (accountType.equals("recruiter")) {
                if (session.getAttribute("name") == null)
                    session.setAttribute("name", recruiterService.findByEmail((String) session.getAttribute("userId")).getName());
                return "recruiter/home";
            }
        }
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Applicant applicant = new Applicant();
        ApplicantCredentials applicantCredentials = new ApplicantCredentials();
        applicantCredentials.setApplicant(applicant);
        applicant.setCredentials(applicantCredentials);
        Recruiter recruiter = new Recruiter();
        RecruiterCredentials recruiterCredentials = new RecruiterCredentials();
        recruiterCredentials.setRecruiter(recruiter);
        recruiter.setCredentials(recruiterCredentials);

        model.addAttribute("applicant", applicant);
        model.addAttribute("recruiter", recruiter);
        return "register";
    }

    @PostMapping("/register/applicant")
    public String registerApplicant(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Applicant applicant, @RequestParam MultipartFile resumeFile) {

        if (applicantService.existByEmail(applicant.getEmail())) {
            redirectAttributes.addAttribute("msgError", "The email address is already in use.");
            return "redirect:/error";
        }

        applicant.getCredentials().setUserId(applicant.getEmail());
        applicant.getCredentials().setPassword(BCrypt.hashpw(applicant.getCredentials().getPassword(), BCrypt.gensalt()));
        try {
            applicant.setResume(resumeFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        applicant.getCredentials().setApplicant(applicant);
        applicantService.save(applicant);

        redirectAttributes.addFlashAttribute("msgLogin", "Your account is ready!");
        return "redirect:/login";
    }

    @PostMapping("/register/recruiter")
    public String registerRecruiter(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Recruiter recruiter, @RequestParam MultipartFile logoFile) {

        if (recruiterService.existsByEmail(recruiter.getEmail())) {
            redirectAttributes.addAttribute("msgError", "The email address is already in use.");
            return "redirect:/error";
        }

        recruiter.getCredentials().setUserId(recruiter.getEmail());
        recruiter.getCredentials().setPassword(BCrypt.hashpw(recruiter.getCredentials().getPassword(), BCrypt.gensalt()));
        try {
            recruiter.setLogo(logoFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recruiter.getCredentials().setRecruiter(recruiter);
        recruiterService.save(recruiter);

        redirectAttributes.addFlashAttribute("msgLogin", "Your account is ready!");
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        session.invalidate();
        model.addAttribute("credentials", new Credentials());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/authenticate")
    public String loginUser(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Credentials credentials, HttpSession session, HttpServletResponse response) {
        final int status = credentialsService.authenticate(credentials);
        if (status == 1) {
            session.setAttribute("userId", credentials.getUserId());

            Cookie cookie = new Cookie("accountType", "applicant");
            response.addCookie(cookie);

            return "redirect:/";
        } else if (status == 2) {
            session.setAttribute("userId", credentials.getUserId());

            Cookie cookie = new Cookie("accountType", "recruiter");
            response.addCookie(cookie);

            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("msgLogin", "Incorrect Credentials");
            return "redirect:/login";
        }
    }

    @GetMapping("/file/logo/{email}")
    public ResponseEntity<byte[]> getLogo(@PathVariable String email) {
        Recruiter recruiter = recruiterService.findByEmail(email);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recruiter.getRecruiterId() + ".jpg\"")
                .body(recruiter.getLogo());
    }

    @GetMapping("/file/resume/{email}")
    public ResponseEntity<byte[]> getResume(@PathVariable String email) {
        Applicant applicant = applicantService.findByEmail(email);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + applicant.getApplicantId() + ".jpg\"")
                .body(applicant.getResume());
    }
}
