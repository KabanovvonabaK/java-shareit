package ru.practicum.shareit.errorHandler;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.errorHandler.exception.*;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {
    private final ErrorHandler handler = new ErrorHandler();

    @Test
    void handleEntityNotFound() {
        EntityNotFoundException e = new EntityNotFoundException("Entity not found");
        ErrorResponse errorResponse = handler.handleEntityNotFound(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleEmailAlreadyExist() {
        EmailAlreadyExistException e = new EmailAlreadyExistException("Email already exist");
        ErrorResponse errorResponse = handler.handleEmailAlreadyExist(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleUserIsNotAnOwner() {
        UserIsNotAnOwnerException e = new UserIsNotAnOwnerException("User is not an owner");
        ErrorResponse errorResponse = handler.handleUserIsNotAnOwner(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleFieldNotFound() {
        FieldNotFoundException e = new FieldNotFoundException("Field not found");
        ErrorResponse errorResponse = handler.handleFieldNotFound(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleThrowable() {
        Throwable e = new Throwable("I'm throwable");
        ErrorResponse errorResponse = handler.handleThrowable(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleBookingTimeException() {
        BookingTimeException e = new BookingTimeException("Time after time");
        ErrorResponse errorResponse = handler.handleBookingTimeException(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleBookingViewException() {
        BookingViewException e = new BookingViewException("Booking view");
        ErrorResponse errorResponse = handler.handleBookingViewException(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleBookingConfirmationException() {
        BookingConfirmationException e = new BookingConfirmationException("Booking confirmation");
        ErrorResponse errorResponse = handler.handleBookingConfirmationException(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleIllegalStateException() {
        IllegalStateException e = new IllegalStateException("I'm illegal!");
        ErrorResponse errorResponse = handler.handleIllegalStateException(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException e = new IllegalArgumentException("I'm illegal too!");
        ErrorResponse errorResponse = handler.handleIllegalArgumentException(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }

    @Test
    void handleUserNotBookedException() {
        UserNotBookedException e = new UserNotBookedException("User not booked");
        ErrorResponse errorResponse = handler.handleUserNotBookedException(e);

        assertAll(
                () -> assertNotNull(errorResponse),
                () -> assertEquals(e.getMessage(), errorResponse.getError())
        );
    }
}