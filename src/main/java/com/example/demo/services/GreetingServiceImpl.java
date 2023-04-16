package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService {
    public String printHello(String name) {
        return "Hello " + name + "!";
    }
}
