package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.mapToShortDto(booking.getItem()),
                UserMapper.toShortUserDto(booking.getBooker()),
                booking.getStatus());
    }

    public static Booking toBooking(BookingRequestDto bookingRequestDto, Booking booking) {
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }

    public static BookingShortDto toShortBookingDto(Booking booking) {
        return new BookingShortDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId());
    }
}