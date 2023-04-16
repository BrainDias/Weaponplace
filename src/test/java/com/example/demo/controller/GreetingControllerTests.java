package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.services.GreetingService;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreetingControllerTests {
	@Mock
	private GreetingService serviceMock;
	@InjectMocks
	private GreetingController greetingControllerWithMocks;
	@Test
	void greetingEndpoint() {
		String name = "Mary";
		Mockito.when(serviceMock.printHello(name)).thenReturn("Hello " + name + "!");
		assertEquals(greetingControllerWithMocks.hello(name), "Hello " + name + "!");
	}
}
