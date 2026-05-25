package com.example.batchfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchFileProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchFileProcessorApplication.class, args);
    }
}

