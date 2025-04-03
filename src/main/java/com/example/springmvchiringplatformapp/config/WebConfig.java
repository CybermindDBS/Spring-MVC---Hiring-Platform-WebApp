package com.example.springmvchiringplatformapp.config;

import com.example.springmvchiringplatformapp.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserAuthInterceptor())
                .addPathPatterns("/applicant/**", "/recruiter/**", "/file/**")
                .excludePathPatterns("/login", "/register", "/");
    }
}
