package com.bdkamaci.githubactivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GithubActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubActivityApplication.class, args);
    }

}
