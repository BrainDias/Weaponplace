package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingControllerTests {

	@Test
	void greetingEndpoint() {
		String name = "Mary";
		assertEquals(new GreetingController().hello(name), "Hello "+name+'!');
	}
}
