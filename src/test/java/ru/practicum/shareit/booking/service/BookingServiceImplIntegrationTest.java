package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private BookingService bookingService;
    private User user;
    private User owner;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemService, userService);
        user = new User();
        user.setName("User Name");
        user.setEmail("user@email.com");
        owner = new User();
        owner.setName("Owner Name");
        owner.setEmail("owner@email.com");
        Item item = new Item();
        item.setOwner(owner);
        item.setAvailable(true);
        item.setName("Item Name");
        item.setDescription("Item Description");
        user = UserMapper.toUser(userService.create(UserMapper.toUserDto(user)));
        owner = UserMapper.toUser(userService.create(UserMapper.toUserDto(owner)));
        item = ItemMapper.toItem(itemService.create(ItemMapper.toItemDto(item), owner.getId()));
        bookingRequestDto = new BookingRequestDto(LocalDateTime.now().plusMinutes(5),
                LocalDateTime.now().plusDays(1), item.getId());
    }

    @Test
    void createBookingByItemOwner() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.create(owner.getId(), bookingRequestDto);
        });

        assertEquals("Impossible to book your own items.", exception.getMessage());
    }

    @Test
    void createBooking() {
        BookingDto bookingDto = bookingService.create(user.getId(), bookingRequestDto);

        assertAll(
                () -> assertNotNull(bookingDto),
                () -> assertEquals(bookingRequestDto.getItemId(), bookingDto.getItem().getId())
        );
    }

    @Test
    void getById() {
        int bookingId = bookingService.create(user.getId(), bookingRequestDto).getId();
        BookingDto bookingServiceById = bookingService.getById(user.getId(), bookingId);

        assertAll(
                () -> assertNotNull(bookingServiceById),
                () -> assertEquals(bookingId, bookingServiceById.getId())
        );
    }

    @Test
    void getByBookerId() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "ALL";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "ALL";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdPast() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "PAST";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdPastPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "PAST";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdCurrent() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "CURRENT";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdCurrentPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "CURRENT";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdFuture() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "FUTURE";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdFuturePaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "FUTURE";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdWaiting() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "WAITING";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdWaitingPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "WAITING";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdRejected() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "REJECTED";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByBookerIdRejectedPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "REJECTED";
        List<BookingDto> byBookerId = bookingService.getByBookerId(user.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByOwnerId() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "ALL";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "ALL";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdPast() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "PAST";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdPastPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "PAST";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdCurrent() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "CURRENT";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdCurrentPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "CURRENT";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdFuture() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "FUTURE";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdFuturePaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "FUTURE";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdWaiting() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "WAITING";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdWaitingPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "WAITING";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(1, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdRejected() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "REJECTED";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state, 0, 10);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void getByOwnerIdRejectedPaged() {
        bookingService.create(user.getId(), bookingRequestDto);
        String state = "REJECTED";
        List<BookingDto> byBookerId = bookingService.getByOwnerId(owner.getId(), state);

        assertAll(
                () -> assertNotNull(byBookerId),
                () -> assertEquals(0, byBookerId.size())
        );
    }

    @Test
    void approveBooking() {
        Boolean approved = true;
        BookingDto bookingDto = bookingService.create(user.getId(), bookingRequestDto);
        BookingDto approve = bookingService.approve(owner.getId(), bookingDto.getId(), approved);

        assertAll(
                () -> assertNotNull(approve),
                () -> assertEquals(BookingStatus.APPROVED, approve.getStatus())
        );
    }
}
