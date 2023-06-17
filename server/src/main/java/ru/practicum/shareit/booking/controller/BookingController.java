package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                                          @RequestParam(name = "state", defaultValue = "ALL") String subState,
                                          @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                          @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return bookingService.getByBookerId(bookerId, subState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String subState,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return bookingService.getByOwnerId(ownerId, subState, from, size);
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