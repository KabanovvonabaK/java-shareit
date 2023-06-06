package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.dto.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
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
class ItemServiceImplTest {
    @Mock
    private final ItemRepository itemRepository;
    @Mock
    private final UserService userService;
    @Mock
    private final BookingRepository bookingRepository;
    @Mock
    private final CommentRepository commentRepository;
    @Mock
    private final ItemRequestRepository itemRequestRepository;
    private ItemService itemService;
    private User user;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userService, commentRepository, bookingRepository, itemRequestRepository);
        user = new User();
        user.setId(1);
        user.setName("Name");
        user.setEmail("email@email.com");

        when(itemRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.getUserById(anyInt()))
                .thenReturn(UserMapper.toUserDto(user));
    }

    @Test
    void create() {
        int userId = 1;
        User user = new User();
        user.setId(2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        when(itemRequestRepository.findById(any()))
                .thenReturn(Optional.of(itemRequest));

        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(itemRequest.getId());

        ItemDto result = itemService.create(itemDto, userId);

        assertAll(
                () -> assertNotNull(result)
        );

        verify(userService, times(1)).getUserById(anyInt());
        verify(itemRequestRepository, times(1)).findById(any());
    }

    @Test
    void updateItem() {
        Item item = new Item();
        item.setId(1);
        item.setOwner(user);
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));

        ItemDto itemDto = new ItemDto();

        ItemDto updateItem = itemService.updateItem(item.getId(), user.getId(), itemDto);

        assertAll(
                () -> assertNotNull(updateItem)
        );
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    void getItemById() {
        Item item = new Item();
        item.setId(1);
        item.setOwner(user);
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.of(item));

        User booker = new User();
        booker.setId(1111);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(booker);
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any()))
                .thenReturn(List.of(booking));

        Comment comment = new Comment();
        comment.setId(1);
        comment.setItem(item);
        comment.setAuthor(booker);
        when(commentRepository.findAllByItemId(anyInt()))
                .thenReturn(List.of(comment));

        ItemDto byItemId = itemService.getByItemId(user.getId(), item.getId());

        assertAll(
                () -> assertNotNull(byItemId)
        );
        verify(itemRepository, times(1)).findById(anyInt());
        verify(commentRepository, times(1)).findAllByItemId(anyInt());
    }

    @Test
    void search() {
        when(itemRepository.search(anyString()))
                .thenReturn(new ArrayList<>());

        List<ItemDto> result = itemService.search("Text");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    void searchPaged() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Text");

        when(itemRepository
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue("Text",
                        "Text", PageRequest.of(0, 10)))
                .thenReturn(Page.empty());

        List<ItemDto> result = itemService.search(user.getId(), "Text", 0, 10);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
        verify(itemRepository, times(1))
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(anyString(),
                        anyString(), any());
    }
}