package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.services.GreetingService;

@RestController
public class GreetingController {

	GreetingService greetingService;

	GreetingController(GreetingService serviceArg) {
		this.greetingService = serviceArg;
	}

	@GetMapping("/greeting")
	public String hello(@RequestParam(value = "name") String name) {
		return greetingService.printHello(name);
	}
}
