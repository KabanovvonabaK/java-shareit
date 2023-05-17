package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getByBookerId(int bookerId, String subState);

    List<BookingDto> getByOwnerId(int ownerId, String subState);

    BookingDto getById(int userId, int bookingId);

    BookingDto create(int userId, BookingInputDto bookingInputDto);

    BookingDto approve(int userId, int bookingId, Boolean isApproved);
}