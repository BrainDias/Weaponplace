package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //ApplicationContext context = new AnnotationConfigApplicationContext("com.example.demo.services");
        SpringApplication.run(Application.class, args);
    }
}