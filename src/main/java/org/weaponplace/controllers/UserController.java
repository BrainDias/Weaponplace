package org.weaponplace.controllers;

import org.weaponplace.dtos.ProfileDTO;
import org.weaponplace.entities.User;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@GetMapping("/users/admin")
	public Page<User> usersAdmin(@RequestParam int pageNum, @RequestParam int size){
		Pageable pageable = PageRequest.of(pageNum, size);
		return userService.pageUsers(pageable);
	}

	@ResponseStatus(HttpStatus.FOUND)
	@GetMapping("/users")
	public List<ProfileDTO> users(@RequestParam int pageNum, @RequestParam int size){
		Pageable pageable = PageRequest.of(pageNum, size);
		return userService.pageUsers(pageable).map(mapper::userToDto).stream().toList();
	}

	//Забанить пользователя от админа
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PatchMapping("/ban/{id}")
	@PreAuthorize("hasAuthority(ROLE_ADMIN)")
	public void ban(@PathVariable Long id){
		userService.banUser(id);
	}

	@PostMapping("/{userId}/rate")
	public HttpStatus rateUser(@PathVariable Long userId, @RequestParam int rating) {
		// Проверяем, что рейтинг от 1 до 5
		if (rating < 1 || rating > 5) {
			return HttpStatus.BAD_REQUEST;
		}

		// Обновляем рейтинг пользователя
		userService.updateRating(userId, rating);
		return HttpStatus.ACCEPTED;
	}
}
