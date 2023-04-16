package com.example.demo.services;

public interface GreetingService {
    default String printHello(String name) {
        return "Hello " + name + "!";
    }
}