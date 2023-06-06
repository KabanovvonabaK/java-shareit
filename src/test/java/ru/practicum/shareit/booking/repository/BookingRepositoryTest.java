package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private Booking booking;
    private Item item;
    private User user;
    final PageRequest pageRequest = PageRequest.of(0, 10);
    private final Clock clock = Clock.systemUTC();

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("User Name");
        user.setEmail("user@email.com");
        userRepository.save(user);
        item = new Item();
        item.setName("Item Name");
        item.setDescription("Description");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(user);
        itemRepository.save(item);
        booking = new Booking();
        booking.setStart(LocalDateTime.now(clock).plusDays(1));
        booking.setEnd(LocalDateTime.now(clock).plusDays(3));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
    }

    @Test
    void findAllByOwnerIdOrderByStartDesc() {
        List<Booking> allByOwnerIdOrderByStartDesc = bookingRepository.findAllByOwnerIdOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(allByOwnerIdOrderByStartDesc),
                () -> assertEquals(1, allByOwnerIdOrderByStartDesc.size())
        );
    }

    @Test
    void findAllByOwnerIdOrderByStartDescPaging() {
        List<Booking> allByOwnerIdOrderByStartDesc = bookingRepository.findAllByOwnerIdOrderByStartDesc(user.getId(),
                pageRequest);

        assertAll(
                () -> assertNotNull(allByOwnerIdOrderByStartDesc),
                () -> assertEquals(1, allByOwnerIdOrderByStartDesc.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStateCurrentOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStateCurrentOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStateCurrentOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStateCurrentOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStatePastOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStatePastOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStatePastOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStatePastOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStateFutureOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStateFutureOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStateFutureOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStateFutureOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStatusOrderByStartDesc(user.getId(), BookingStatus.WAITING);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByOwnerIdAndStatusOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByOwnerIdAndStatusOrderByStartDesc(user.getId(), BookingStatus.WAITING, pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByBookerIdOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStateCurrentOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStateCurrentOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStateCurrentOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStateCurrentOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStatePastOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStatePastOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStatePastOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStatePastOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(0, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStateFutureOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStateFutureOrderByStartDesc(user.getId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStateFutureOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStateFutureOrderByStartDesc(user.getId(), pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(user.getId(), BookingStatus.WAITING);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDescPaging() {
        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(user.getId(), BookingStatus.WAITING, pageRequest);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size())
        );
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc() {
        Optional<Booking> result = bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(item.getId(),
                        LocalDateTime.now(clock), BookingStatus.WAITING);

        assertAll(
                () -> assertNotNull(result)
        );
    }

    @Test
    void findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc() {
        Optional<Booking> result = bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(item.getId(),
                        LocalDateTime.now(clock), BookingStatus.WAITING);

        assertAll(
                () -> assertNotNull(result)
        );
    }

    @Test
    void existsByBookerIdAndItemIdAndEndBefore() {
        Boolean result = bookingRepository
                .existsByBookerIdAndItemIdAndEndBefore(user.getId(),
                        item.getId(), LocalDateTime.now(clock));

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result)
        );
    }
}