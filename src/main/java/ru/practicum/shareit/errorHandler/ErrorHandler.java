package ru.practicum.shareit.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.errorHandler.exception.EmailAlreadyExistException;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.errorHandler.exception.FieldNotFoundException;
import ru.practicum.shareit.errorHandler.exception.UserIsNotAnOwnerException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFound(final EntityNotFoundException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExist(final EmailAlreadyExistException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUserIsNotAnOwner(final UserIsNotAnOwnerException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFieldNotFound(final FieldNotFoundException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }
}