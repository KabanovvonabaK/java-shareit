package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getByBookerId(int bookerId, String subState);

    List<BookingDto> getByBookerId(int bookerId, String subState, int from, int size);

    List<BookingDto> getByOwnerId(int ownerId, String subState);

    List<BookingDto> getByOwnerId(int ownerId, String subState, int from, int size);

    BookingDto getById(int userId, int bookingId);

    BookingDto create(int userId, BookingRequestDto bookingRequestDto);

    BookingDto approve(int userId, int bookingId, Boolean isApproved);
}