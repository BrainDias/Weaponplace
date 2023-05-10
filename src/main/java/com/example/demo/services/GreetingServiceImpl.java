package com.example.demo.services;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingServiceImpl implements GreetingService {

    private final UserRepository userRepository;

    public String printHello(String name) {
        return "Hello " + userRepository.findUserByName(name).getTitle() + " " + name + "!";
    }
}
