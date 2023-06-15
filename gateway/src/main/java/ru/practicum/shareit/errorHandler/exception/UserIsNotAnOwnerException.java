package ru.practicum.shareit.errorHandler.exception;

public class UserIsNotAnOwnerException extends RuntimeException {
    public UserIsNotAnOwnerException(String message) {
        super(message);
    }
}