package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    @Mock
    private final ItemRepository itemRepository;
    @Mock
    private final ItemService itemService;
    @Mock
    private final UserRepository userRepository;
    @Mock
    private final UserService userService;
    @Mock
    private final BookingRepository bookingRepository;
    private BookingService bookingService;
    private User user;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemService, userService);
        user = new User();
        user.setId(1);
        user.setName("Name");
        user.setEmail("email@email.com");

        when(bookingRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.getUserById(user.getId()))
                .thenReturn(UserMapper.toUserDto(user));
    }

    @Test
    void getByBookerId() {
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byBookerId = bookingService.getByBookerId(1,
                "ALL", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertTrue(byBookerId.isEmpty())
        );
        verify(bookingRepository, times(1)).findAllByBookerIdOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void create() {
        int userId = 1;
        User user = new User();
        user.setId(2);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        BookingRequestDto bookingRequestDto = new BookingRequestDto(LocalDateTime.now().plusMinutes(5),
                LocalDateTime.now().plusDays(1), 1);

        Item item = new Item();
        item.setId(3);
        item.setOwner(user);
        item.setAvailable(true);

        when(itemService.getByItemId(anyInt(), anyInt()))
                .thenReturn(ItemMapper.toItemDto(item));

        BookingDto bookingDto = bookingService.create(userId, bookingRequestDto);

        assertAll(
                () -> assertNotNull(bookingDto)
        );

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void setApproved() {
        Boolean approved = true;
        Item item = new Item();
        item.setId(3);
        item.setOwner(user);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setId(3);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));
        when(itemService.getByItemId(user.getId(), item.getId()))
                .thenReturn(ItemMapper.toItemDto(item));

        BookingDto approve = bookingService.approve(user.getId(), booking.getId(), approved);

        assertAll(
                () -> assertNotNull(approve),
                () -> assertEquals(BookingStatus.APPROVED, approve.getStatus())
        );
        verify(bookingRepository, times(1)).findById(anyInt());
        verify(itemService, times(1)).getByItemId(anyInt(), anyInt());
    }

    @Test
    void setRejected() {
        Boolean approved = false;
        Item item = new Item();
        item.setId(3);
        item.setOwner(user);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setId(3);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setBooker(user);
        booking.setItem(item);

        when(bookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));
        when(itemService.getByItemId(user.getId(), item.getId()))
                .thenReturn(ItemMapper.toItemDto(item));

        BookingDto approve = bookingService.approve(user.getId(), booking.getId(), approved);

        assertAll(
                () -> assertNotNull(approve),
                () -> assertEquals(BookingStatus.REJECTED, approve.getStatus())
        );
        verify(bookingRepository, times(1)).findById(anyInt());
        verify(itemService, times(1)).getByItemId(anyInt(), anyInt());
    }

    @Test
    void getPastBookingsBooker() {
        when(bookingRepository.findAllByBookerIdAndStatePastOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byBookerId = bookingService.getByBookerId(1,
                "PAST", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertTrue(byBookerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatePastOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void getFutureBookingsBooker() {
        when(bookingRepository.findAllByBookerIdAndStateFutureOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byBookerId = bookingService.getByBookerId(1,
                "FUTURE", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertTrue(byBookerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStateFutureOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void getCurrentBookingsBooker() {
        when(bookingRepository.findAllByBookerIdAndStateCurrentOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byBookerId = bookingService.getByBookerId(1,
                "CURRENT", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertTrue(byBookerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStateCurrentOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void getPastBookingsOwner() {
        when(bookingRepository.findAllByOwnerIdAndStatePastOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "PAST", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdAndStatePastOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void getFutureBookingsOwner() {
        when(bookingRepository.findAllByOwnerIdAndStateFutureOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "FUTURE", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdAndStateFutureOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void getCurrentBookingsOwner() {
        when(bookingRepository.findAllByOwnerIdAndStateCurrentOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "CURRENT", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdAndStateCurrentOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void findAllByOwnerIdOrderByStartDesc() {
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "ALL");

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdOrderByStartDesc(anyInt());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void findAllByOwnerIdOrderByStartDescPaged() {
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "ALL", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStartDesc() {
        when(bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "WAITING");

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdAndStatusOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStartDescPaged() {
        when(bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byOwnerId = bookingService.getByOwnerId(1,
                "WAITING", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byOwnerId),
                () -> assertTrue(byOwnerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByOwnerIdAndStatusOrderByStartDesc(anyInt(), any(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byBookerId = bookingService.getByBookerId(1,
                "REJECTED");

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertTrue(byBookerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDescPaged() {
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(new ArrayList<>());

        List<BookingDto> byBookerId = bookingService.getByBookerId(1,
                "REJECTED", PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertTrue(byBookerId.isEmpty())
        );
        verify(bookingRepository, times(1))
                .findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), any());
        verify(userService, times(1)).getUserById(anyInt());
    }
}