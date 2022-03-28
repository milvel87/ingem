package com.ingem.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ingem.interview.controller", "com.ingem.interview.model", "com.ingem.interview.repository", "com.ingem.interview.util", "com.ingem.interview.exception"})
@EnableJpaRepositories(basePackages = "com.ingem.interview.repository")
public class InterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }

}
