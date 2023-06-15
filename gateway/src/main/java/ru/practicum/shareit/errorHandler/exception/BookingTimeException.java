package ru.practicum.shareit.errorHandler.exception;

public class BookingTimeException extends RuntimeException {
    public BookingTimeException(String message) {
        super(message);
    }
}