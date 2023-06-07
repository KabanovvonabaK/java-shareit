package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private final int userId = 1;

    private final UserDto userDto = new UserDto(
            1,
            "User Name",
            "user@email.com"
    );

    private final UserDto userDtoResponse = new UserDto(
            1,
            "User Name",
            "user@email.com"
    );

    @Test
    void getAll() throws Exception {
        when(userService.getAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users")
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).getAll();
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoResponse);

        mockMvc.perform(get("/users/" + userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(userDtoResponse.getName()))
                .andExpect(jsonPath("$.email").value(userDtoResponse.getEmail()));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void createUser() throws Exception {
        when(userService.create(any()))
                .thenReturn(userDtoResponse);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(userDtoResponse.getName()))
                .andExpect(jsonPath("$.email").value(userDtoResponse.getEmail()));
        verify(userService, times(1)).create(userDto);
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(anyInt(), any()))
                .thenReturn(userDtoResponse);

        mockMvc.perform(patch("/users/" + userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(userDtoResponse.getName()))
                .andExpect(jsonPath("$.email").value(userDtoResponse.getEmail()));
        verify(userService, times(1)).updateUser(anyInt(), any());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/" + userId)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUser(anyInt());
    }
}