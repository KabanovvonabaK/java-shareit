package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplIntegrationTest {
    private UserService userService;
    private final UserRepository userRepository;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        userDto = new UserDto();
        userDto.setName("User Name");
        userDto.setEmail("user@email.com");
    }

    @Test
    void createUser() {
        UserDto dto = userService.create(userDto);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(userDto.getName(), dto.getName()),
                () -> assertEquals(userDto.getEmail(), dto.getEmail())
        );
    }

    @Test
    void getUserById() {
        int userId = userService.create(userDto).getId();
        UserDto userById = userService.getUserById(userId);

        assertAll(
                () -> assertNotNull(userById),
                () -> assertEquals(userId, userById.getId())
        );
    }

    @Test
    void updateUser() {
        int userId = userService.create(userDto).getId();
        userDto.setName("New Cool Name");
        UserDto dto = userService.updateUser(userId, userDto);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(userDto.getName(), dto.getName())
        );
    }

    @Test
    void deleteUser() {
        int userId = userService.create(userDto).getId();
        userService.deleteUser(userId);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAll() {
        List<UserDto> all = userService.getAll();

        assertAll(
                () -> assertNotNull(all),
                () -> assertEquals(0, all.size())
        );
    }
}