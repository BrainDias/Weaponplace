package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.services.GreetingService;

class GreetingControllerTests {

	@Test
	void greetingEndpoint() {
		String name = "Mary";
		assertEquals(new GreetingController(new GreetingService()).hello(name), "Hello " + name + "!");
	}
}
