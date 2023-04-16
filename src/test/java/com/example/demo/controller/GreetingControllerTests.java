package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static  org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.services.GreetingService;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreetingControllerTests {
	@Mock
	private GreetingService greetingService;
	@InjectMocks
	private GreetingController greetingController;
	@Test
	void greetingEndpoint() {
		String name = "Mary";
		when(greetingService.printHello(name)).thenReturn("Hello Mary!");
		assertEquals(greetingController.hello(name), "Hello Mary!");
	}
}
