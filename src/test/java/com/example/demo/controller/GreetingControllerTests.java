package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.services.GreetingService;

class GreetingControllerTests {

@InjectMocks
GreetingService serviceMock;
	@Test
	void greetingEndpoint() {
		String name = "Mary";
		Mockito.when(serviceMock.printHello(name)).thenReturn("Hello "+name+"!");
		assertEquals(new GreetingController(serviceMock).hello(name), "Hello " + name + "!");
	}
}
