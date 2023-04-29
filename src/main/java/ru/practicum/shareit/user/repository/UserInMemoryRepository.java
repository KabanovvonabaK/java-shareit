package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorHandler.exception.EmailAlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserInMemoryRepository implements UserRepository {

    List<User> users = new ArrayList<>();
    private int id = 1;

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        user.setId(id++);
        users.add(user);
        return user;
    }

    @Override
    public User get(int id) {
        checkUserExist(id);
        User user = new User();
        for (User u : users) {
            if (u.getId() == id) {
                user = u;
            }
        }
        return user;
    }

    @Override
    public User update(int userId, User user) {
        checkUserExist(userId);
        checkEmail(userId, user.getEmail());
        for (User u : users) {
            if (u.getId() == userId) {
                if (user.getName() != null) {
                    u.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    u.setEmail(user.getEmail());
                }
            }
        }
        return get(userId);
    }

    @Override
    public void delete(int id) {
        checkUserExist(id);
        users.removeIf(u -> u.getId() == id);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    public void checkUserExist(int id) {
        List<Integer> ids = new ArrayList<>();
        for (User u : users) {
            ids.add(u.getId());
        }
        if (!ids.contains(id)) {
            throw new EntityNotFoundException(String.format("User with id %s not exist", id));
        }
    }

    private void checkEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                throw new EmailAlreadyExistException(String.format("User with email %s already exist", email));
            }
        }
    }

    private void checkEmail(int userId, String email) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getId() != userId) {
                throw new EmailAlreadyExistException(String.format("User with email %s already exist", email));
            }
        }
    }
}