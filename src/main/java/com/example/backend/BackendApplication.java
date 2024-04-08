package com.example.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class BackendApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String port;
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started. Listen on port:" + port);
    }
}
