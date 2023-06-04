package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.dto.Comment;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentRequestDto;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private ItemService itemService;
    private User user;
    private User owner;
    private ItemRequest itemRequest;
    private ItemDto itemDto;
    private Booking booking;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(itemRepository,
                userService, commentRepository, bookingRepository, itemRequestRepository);
        user = new User();
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
        itemDto = new ItemDto();
        itemDto.setName("Item Name");
        itemDto.setDescription("Description");
        itemDto.setAvailable(Boolean.TRUE);
        itemDto.setOwner(UserMapper.toShortUserDto(owner));
        itemDto.setRequestId(itemRequest.getId());
        itemRepository.save(ItemMapper.toItem(itemDto));
        booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        comment = new Comment();
        comment.setText("Text");
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    void create() {
        ItemDto dto = itemService.create(itemDto, owner.getId());

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(itemDto.getName(), dto.getName()),
                () -> assertEquals(itemRequest.getId(), dto.getRequestId())
        );
    }

    @Test
    void updateItem() {
        int itemId = itemService.create(itemDto, owner.getId()).getId();
        itemDto.setName("New Name");
        itemDto.setDescription("New Description");
        ItemDto updateItem = itemService.updateItem(itemId, owner.getId(), itemDto);

        assertAll(
                () -> assertNotNull(updateItem),
                () -> assertEquals(itemDto.getName(), updateItem.getName()),
                () -> assertEquals(itemDto.getDescription(), updateItem.getDescription())
        );
    }

    @Test
    void getByItemId() {
        ItemDto dto = itemService.create(itemDto, owner.getId());
        Item item = ItemMapper.toItem(dto);
        item.setId(dto.getId());
        booking.setItem(item);
        comment.setItem(item);
        bookingRepository.save(booking);
        commentRepository.save(comment);

        ItemDto byItemId = itemService.getByItemId(owner.getId(), item.getId());

        assertAll(
                () -> assertNotNull(byItemId),
                () -> assertEquals(1, byItemId.getComments().size()),
                () -> assertEquals(booking.getBooker().getId(), byItemId.getNextBooking().getBookerId())
        );
    }

    @Test
    void search() {
        itemService.create(itemDto, owner.getId());
        String text = "Item";
        List<ItemDto> search = itemService.search(user.getId(), text, PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(search),
                () -> assertEquals(2, search.size())
        );
    }

    @Test
    void addComment() {
        ItemDto dto = itemService.create(itemDto, owner.getId());
        Item item = ItemMapper.toItem(dto);
        item.setId(dto.getId());
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(4));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        bookingRepository.save(booking);
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Comment");

        CommentDto addComment = itemService.addComment(user.getId(), item.getId(), commentRequestDto);

        assertAll(
                () -> assertNotNull(addComment),
                () -> assertEquals(user.getName(), addComment.getAuthorName())
        );
    }
}
