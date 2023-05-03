package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAll() {
        log.info("Attempt to get all users.");
        return userRepository.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto create(UserDto user) {
        log.info("Attempt to create new user {}", user);
        return UserMapper.toUserDto(userRepository.create(UserMapper.toUser(user)));
    }

    public UserDto getUserById(int id) {
        log.info("Attempt to get user with id {}", id);
        userRepository.checkUserExist(id);
        return UserMapper.toUserDto(userRepository.get(id));
    }

    public UserDto updateUser(int userId, UserDto userDto) {
        log.info("Attempt to updated user {}", userDto);
        userRepository.checkUserExistAndEmail(userId, userDto.getEmail());
        User user = userRepository.get(userId);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(user);
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