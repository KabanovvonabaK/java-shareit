package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<UserDto> getAll() {
        log.info("Attempt to get all users.");
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto create(UserDto userDto) {
        log.info("Attempt to create new user {}", userDto);
        User user = UserMapper.toUser(userDto, new User());
        return Optional.of(userRepository.save(user)).map(UserMapper::toUserDto).orElseThrow();
    }

    public UserDto getUserById(int id) {
        log.info("Attempt to get user with id {}", id);
        return userRepository
                .findById(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not exist", id)));
    }

    public UserDto updateUser(int userId, UserDto userDto) {
        log.info("Attempt to updated user {}", userDto);
        User oldUser = UserMapper.toUser(getUserById(userId));
        UserDto user = Optional.of(userRepository.save(UserMapper.toUser(userDto, oldUser)))
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %s not exist", userId)));

        return user;
    }

    public void deleteUser(int id) {
        log.info("Attempt to delete user with id {}", id);
        User user = UserMapper.toUser(getUserById(id));
        userRepository.deleteById(user.getId());
    }
}