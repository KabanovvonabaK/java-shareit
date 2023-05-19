package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utils.Create;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getByBookerId(@RequestHeader("X-Sharer-User-Id") int bookerId,
                                          @RequestParam(name = "state", defaultValue = "ALL") String subState) {
        return bookingService.getByBookerId(bookerId, subState);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String subState) {
        return bookingService.getByOwnerId(ownerId, subState);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                             @Validated(Create.class) @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId,
                              @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.approve(userId, bookingId, isApproved);
    }
}