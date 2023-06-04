package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    @MockBean
    private final UserRepository userRepository;
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        userDto = new UserDto();
        userDto.setName("User Name");
        userDto.setEmail("user@email.com");

        when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

    }

    @Test
    void getAll() {
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<UserDto> all = userService.getAll();

        assertAll(
                () -> assertNotNull(all),
                () -> assertTrue(all.isEmpty())
        );
    }

    @Test
    void create() {
        UserDto dto = userService.create(userDto);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(userDto.getName(), dto.getName())
        );
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getUserById() {
        int userId = 1111;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(userId));
        assertEquals("User with id " + userId + " not exist", entityNotFoundException.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }
}