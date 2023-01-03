package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import java.util.*;

@SpringBootTest
class GreetingControllerTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test_JUnit() {
		String name = "Mary";
		Assertions.assertLinesMatch(List.of(GreetingController.hello(name)), List.of("Hello "+name+'!'));
	}
}
