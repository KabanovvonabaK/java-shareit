package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto create(UserDto userDto);

    UserDto getUserById(int id);

    UserDto updateUser(int userId, UserDto userDto);

    void deleteUser(int id);

    void checkUserExist(int userId);
}