package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus bookingStatus;
    private int bookerId;
}