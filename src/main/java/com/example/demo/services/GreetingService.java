package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public interface GreetingService {
    default String printHello(String name) {
        return "Hello " + name + "!";
    }
}