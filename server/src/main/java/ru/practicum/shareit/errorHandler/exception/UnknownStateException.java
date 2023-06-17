package ru.practicum.shareit.errorHandler.exception;

public class UnknownStateException extends IllegalStateException {
    public UnknownStateException(String s) {
        super(String.format("Unknown state: %s", s));
    }
}