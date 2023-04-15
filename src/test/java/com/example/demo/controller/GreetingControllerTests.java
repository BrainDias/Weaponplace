package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.services.GreetingService;

class GreetingControllerTests {

	@Test
	void greetingEndpoint() {
		String name = "Mary";
		GreetingService ServiceMock = Mockito.mock(GreetingService.class);
		Mockito.when(ServiceMock.printHello(name)).thenReturn("Hello "+name+"!");
		assertEquals(new GreetingController(ServiceMock).hello(name), "Hello " + name + "!");
	}
}
