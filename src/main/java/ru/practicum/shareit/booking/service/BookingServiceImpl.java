package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.errorHandler.exception.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemService itemService;

    private final UserService userService;

    @Override
    public List<BookingDto> getByBookerId(int bookerId, String subState) {
        log.info("Attempt to get by bookerId {}", bookerId);
        BookingState bookingState = getState(subState);
        User booker = UserMapper.toUser(userService.getUserById(bookerId));
        List<Booking> bookings = new ArrayList<>();

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndStatePastOrderByStartDesc(booker.getId());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStateCurrentOrderByStartDesc(booker.getId());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStateFutureOrderByStartDesc(booker.getId());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                        BookingStatus.REJECTED);
                break;
        }

        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getByOwnerId(int ownerId, String subState) {
        log.info("Attempt to get booking by ownerId {}", ownerId);
        BookingState bookingState = getState(subState);
        User owner = UserMapper.toUser(userService.getUserById(ownerId));
        List<Booking> bookings = new ArrayList<>();

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(owner.getId());
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndStatePastOrderByStartDesc(owner.getId());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStateCurrentOrderByStartDesc(owner.getId());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStateFutureOrderByStartDesc(owner.getId());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(owner.getId(),
                        BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(owner.getId(),
                        BookingStatus.REJECTED);
                break;
        }

        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getById(int userId, int bookingId) {
        log.info("Attempt to get booking by id {}", bookingId);
        Booking booking = getBookingById(bookingId);
        User booker = booking.getBooker();
        User owner = UserMapper.toUser(userService.getUserById(booking.getItem().getOwner().getId()));
        if (booker.getId() != userId && owner.getId() != userId) {
            throw new BookingViewException("Booking can be viewed only by author or item owner.");
        }
        return bookingRepository
                .findById(bookingId)
                .map(BookingMapper::toBookingDto)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Booking with id %s not exist", bookingId)));
    }

    @Transactional
    @Override
    public BookingDto create(int userId, BookingInputDto bookingInputDto) {
        log.info("Creating booking by user with id {}", userId);
        User booker = UserMapper.toUser(userService.getUserById(userId));
        Item item = ItemMapper.toItem(itemService.getByItemId(userId, bookingInputDto.getItemId()));

        if (booker.getId() == item.getOwner().getId()) {
            throw new IllegalArgumentException("Impossible to book your own items.");
        }
        if (!item.getAvailable()) {
            throw new IllegalStateException(String.format("Item with id %s is unavailable", item.getId()));
        }
        if (bookingInputDto.getEnd().isBefore(bookingInputDto.getStart())
                || bookingInputDto.getEnd().equals(bookingInputDto.getStart())
                || bookingInputDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingTimeException("End must be after start");
        }

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);

        return Optional.of(bookingRepository.save(BookingMapper.toBooking(bookingInputDto, booking)))
                .map(BookingMapper::toBookingDto)
                .orElseThrow();
    }

    @Transactional
    @Override
    public BookingDto approve(int userId, int bookingId, Boolean isApproved) {
        log.info("Updating status for booking with id {}", bookingId);
        User owner = UserMapper.toUser(userService.getUserById(userId));
        Booking booking = getBookingById(bookingId);
        Item item = ItemMapper.toItem(itemService.getByItemId(userId, booking.getItem().getId()));
        BookingStatus newBookingStatus = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        if (!booking.getStatus().equals(BookingStatus.WAITING) && owner.getId() == item.getOwner().getId()) {
            throw new IllegalStateException(String.format("Can't update booking with id %s", bookingId));
        }
        if (owner.getId() != item.getOwner().getId()) {
            throw new BookingConfirmationException("Booking can be confirmed only by it's owner");
        }
        booking.setStatus(newBookingStatus);
        return BookingMapper.toBookingDto(booking);
    }

    private BookingState getState(String bookingState) {
        try {
            return BookingState.valueOf(bookingState);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(bookingState);
        }
    }

    private Booking getBookingById(int bookingId) {
        return bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Booking with id %s not exist", bookingId)));
    }
}