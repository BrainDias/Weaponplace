package com.example.demo.controllers;

import com.example.demo.dtos.ProfileDTO;
import com.example.demo.entities.User;
import com.example.demo.mappers.DtoMapper;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final DtoMapper mapper;

	//Личный кабинет
	@ResponseStatus(HttpStatus.FOUND)
	@GetMapping("/profile")
	public ProfileDTO account(@AuthenticationPrincipal User currentUser){
		return mapper.userToDto(currentUser);
	}

	//Вернуть пользователей постранично для админа
	@ResponseStatus(HttpStatus.FOUND)
	@GetMapping("/users")
	//TODO: Return Specifical DTOs for admins
	//TODO: Фильтрация по наличию определенных продуктов
	public Page<User> users(@RequestParam int pageNum, @RequestParam int size){
		Pageable pageable = PageRequest.of(pageNum, size);
		return userService.pageUsers(pageable);
	}

	//Забанить пользователя от админа
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PatchMapping("/ban/{id}")
	//TODO: Make url accessible for admins only
	public void ban(@PathVariable Long id){
		userService.banUser(id);
	}
}
