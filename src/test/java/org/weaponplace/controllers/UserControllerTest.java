package org.weaponplace.controllers;

import org.weaponplace.dtos.ProfileDTO;
import org.weaponplace.entities.User;
import org.weaponplace.mappers.DtoMapper;
import org.weaponplace.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private DtoMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void account_shouldReturnProfileDTO() throws Exception {
        // Arrange
        User currentUser = new User();
        ProfileDTO profileDTO = new ProfileDTO();
        when(mapper.userToDto(currentUser)).thenReturn(profileDTO);

        // Act & Assert
        mockMvc.perform(get("/profile"))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(profileDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void usersAdmin_shouldReturnPagedUsers() throws Exception {
        // Arrange
        List<User> users = List.of(new User(), new User());
        Page<User> page = new PageImpl<>(users);
        when(userService.pageUsers(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/users/admin")
                        .param("pageNum", "0")
                        .param("size", "2"))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(page.getContent())));
    }

    @Test
    @WithMockUser
    void users_shouldReturnProfileDTOList() throws Exception {
        // Arrange
        List<User> users = List.of(new User(), new User());
        List<ProfileDTO> profileDTOList = List.of(new ProfileDTO(), new ProfileDTO());
        Page<User> page = new PageImpl<>(users);
        when(userService.pageUsers(any(Pageable.class))).thenReturn(page);
        when(mapper.userToDto(any(User.class))).thenReturn(profileDTOList.get(0), profileDTOList.get(1));

        // Act & Assert
        mockMvc.perform(get("/users")
                        .param("pageNum", "0")
                        .param("size", "2"))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(profileDTOList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ban_shouldBanUser() throws Exception {
        // Arrange
        doNothing().when(userService).banUser(1L);

        // Act & Assert
        mockMvc.perform(patch("/ban/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser
    void rateUser_shouldReturnBadRequestIfInvalidRating() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/1/rate")
                        .param("rating", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void rateUser_shouldAcceptValidRating() throws Exception {
        // Arrange
        doNothing().when(userService).updateRating(1L, 5);

        // Act & Assert
        mockMvc.perform(post("/1/rate")
                        .param("rating", "5"))
                .andExpect(status().isAccepted());
    }
}

