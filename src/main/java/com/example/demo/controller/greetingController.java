package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.services.GreetingService;

@RestController
public class GreetingController {

	GreetingService service;

	GreetingController(GreetingService serviceArg) {
		service = serviceArg;
	}

	@GetMapping("/greeting")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return service.printHello(name);
	}
}
