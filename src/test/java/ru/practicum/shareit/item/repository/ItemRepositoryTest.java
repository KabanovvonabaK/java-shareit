package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.dto.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    private User owner;
    private ItemRequest itemRequest;
    final PageRequest pageRequest = PageRequest.of(0, 10);

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setName("User Name");
        user.setEmail("user@email.com");
        owner = new User();
        owner.setName("Owner Name");
        owner.setEmail("owner@email.com");
        userRepository.save(user);
        userRepository.save(owner);
        itemRequest = new ItemRequest();
        itemRequest.setDescription("Description");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        Item item = new Item();
        item.setName("Item Name");
        item.setDescription("Description");
        item.setAvailable(Boolean.TRUE);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        itemRepository.save(item);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        Comment comment = new Comment();
        comment.setText("Text");
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        commentRepository.save(comment);
    }

    @Test
    void findAllByOwnerId() {
        List<Item> itemList = itemRepository
                .findAllByOwnerId(owner.getId(), pageRequest).stream().collect(Collectors.toList());

        assertAll(
                () -> assertNotNull(itemList),
                () -> assertEquals(1, itemList.size())
        );
    }

    @Test
    void findAllByRequestIdIn() {
        List<Item> requestIdIn = itemRepository.findAllByRequestIdIn(List.of(itemRequest.getId()));

        assertAll(
                () -> assertNotNull(requestIdIn),
                () -> assertEquals(1, requestIdIn.size())
        );
    }

    @Test
    void findAllByRequestId() {
        List<Item> allByRequestId = itemRepository.findAllByRequestId(itemRequest.getId());

        assertAll(
                () -> assertNotNull(allByRequestId),
                () -> assertEquals(1, allByRequestId.size())
        );
    }

    @Test
    void findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue() {
        String text = "Name";
        List<Item> itemList = itemRepository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text,
                text, pageRequest).stream().collect(Collectors.toList());

        assertAll(
                () -> assertNotNull(itemList),
                () -> assertEquals(1, itemList.size())
        );
    }
}