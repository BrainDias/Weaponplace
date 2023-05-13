package com.example.demo.controllers;

import com.example.demo.services.GreetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GreetingControllerTests {

    @Mock
    private GreetingService greetingService;
    @InjectMocks
    private GreetingController greetingController;

    @Test
    void shouldReturnGreetingWithName() {
        String name = "Mary";
        when(greetingService.printHello(name)).thenReturn("Hello Mrs. Mary!");
        assertEquals(greetingController.hello(name), "Hello Mrs. Mary!");
        verify(greetingService).printHello("Mary");
        verifyNoMoreInteractions(greetingService);
    }
}
