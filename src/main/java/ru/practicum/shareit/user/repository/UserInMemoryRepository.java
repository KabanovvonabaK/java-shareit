package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorHandler.exception.EmailAlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserInMemoryRepository implements UserRepository {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        return users.get(id);
    }

    @Override
    public User update(int userId, User user) {
        users.put(userId, user);
        return get(userId);
    }

    @Override
    public void delete(int id) {
        checkUserExist(id);
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public void checkUserExist(int id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException(String.format("User with id %s not exist", id));
        }
    }

    public void checkEmail(String email) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new EmailAlreadyExistException(String.format("User with email %s already exist", email));
        }
    }

    public void checkEmail(int userId, String email) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equals(email) && user.getId() != userId)) {
            throw new EmailAlreadyExistException(String.format("User with email %s already exist", email));
        }
    }
}