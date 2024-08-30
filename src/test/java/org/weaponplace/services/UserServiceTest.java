package org.weaponplace.services;

import org.weaponplace.entities.User;
import org.weaponplace.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;



    private User user;
    private User anotherUser;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        anotherUser = new User();


        // Initialize user
        user.setRating(4.0f);
        user.setRatingsNumber(5);
        anotherUser.setWishFilters(new ArrayList<>());
    }

    @Test
    void testPageUsers() {
        Pageable pageable = Pageable.ofSize(10);
        List<User> users = new ArrayList<>();
        Page<User> page = new PageImpl<>(users);
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.pageUsers(pageable);

        assertEquals(page, result);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testBanUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.banUser(1L);

        verify(userRepository).findById(anyLong());
        verify(userRepository).save(user);
        assertFalse(user.isEnabled());
    }



    @Test
    void testGetUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findById(anyLong());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.getUser(1L));
        assertEquals("User doesn't exist", thrown.getMessage());
        verify(userRepository).findById(anyLong());
    }



    @Test
    void testUpdateRating() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateRating(1L, 5);

        assertEquals(4.2f, user.getRating());
        assertEquals(6, user.getRatingsNumber());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateRatingUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> userService.updateRating(1L, 5));
        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}

