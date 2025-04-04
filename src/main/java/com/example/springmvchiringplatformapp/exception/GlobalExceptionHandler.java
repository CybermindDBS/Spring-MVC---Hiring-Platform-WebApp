package com.example.springmvchiringplatformapp.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, RedirectAttributes redirectAttributes) {
        if (ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException) {
            throw (org.springframework.web.multipart.MaxUploadSizeExceededException) ex;
        }
        redirectAttributes.addAttribute("msgError", ex.getMessage() + "\n\n" + (ex.getCause() == null ? "" : ex.getCause()));
        return "redirect:/error";
    }
}