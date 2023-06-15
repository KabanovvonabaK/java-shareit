package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.errorHandler.exception.UnknownStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingStatus state = BookingStatus.from(stateParam)
                .orElseThrow(() -> new UnknownStateException(stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookingRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId,
                              @RequestParam(name = "approved") Boolean isApproved) {
        return bookingClient.approve(userId, bookingId, isApproved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestParam(name = "state", defaultValue = "ALL") String subState,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        BookingStatus state = BookingStatus.from(subState)
                .orElseThrow(() -> new UnknownStateException(subState));
        return bookingClient.getByOwnerId(ownerId, state, from, size);
    }
}