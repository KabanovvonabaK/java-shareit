package ru.practicum.shareit.errorHandler.exception;

public class UserNotBookedException extends RuntimeException {
    public UserNotBookedException(String message) {
        super(message);
    }
}