package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User create(User user);

    User get(int id);

    User update(int userId, User user);

    void delete(int id);

    List<User> getAll();

    void checkUserExist(int userId);

    void checkEmail(String email);

    void checkUserExistAndEmail(int userId, String email);
}