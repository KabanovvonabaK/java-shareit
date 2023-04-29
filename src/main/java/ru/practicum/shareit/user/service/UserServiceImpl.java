package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        log.info("Attempt to get all users.");
        return userRepository.getAll();
    }

    public User create(UserDto user) {
        log.info("Attempt to create new user {}", user);
        return userRepository.create(UserMapper.toUser(user));
    }

    public User getUserById(int id) {
        log.info("Attempt to get user with id {}", id);
        return userRepository.get(id);
    }

    public User updateUser(int userId, UserDto userDto) {
        log.info("Attempt to updated user {}", userDto);
        return userRepository.update(userId, UserMapper.toUser(userDto));
    }

    public void deleteUser(int id) {
        log.info("Attempt to delete user with id {}", id);
        userRepository.delete(id);
    }

    public void checkUserExist(int id) {
        log.info("Checking that user with id {} exist", id);
        userRepository.checkUserExist(id);
    }
}