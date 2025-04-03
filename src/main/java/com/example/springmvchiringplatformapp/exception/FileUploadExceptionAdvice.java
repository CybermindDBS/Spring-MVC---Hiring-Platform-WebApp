package com.example.springmvchiringplatformapp.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("msgError", "File size exceeds allowed limit.");
        return "redirect:/error";
    }
}
