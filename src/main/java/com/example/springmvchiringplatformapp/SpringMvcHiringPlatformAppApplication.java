package com.example.springmvchiringplatformapp;

import com.example.springmvchiringplatformapp.service.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringMvcHiringPlatformAppApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringMvcHiringPlatformAppApplication.class, args);

        Service service = context.getBean(Service.class);
        service.setup();
        service.run1();
    }

}
