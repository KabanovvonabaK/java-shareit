package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User create(UserDto userDto);

    User getUserById(int id);

    User updateUser(int userId, UserDto userDto);

    void deleteUser(int id);

    void checkUserExist(int userId);
}