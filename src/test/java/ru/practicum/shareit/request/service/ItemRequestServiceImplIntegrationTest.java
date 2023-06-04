package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplIntegrationTest {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private ItemRequestService itemRequestService;
    private ItemRequestDto itemRequestDto;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    public void setUp() {
        itemRequestService = new ItemRequestServiceImpl(userService, itemService, itemRequestRepository);

        user = new User();
        user.setName("Name");
        user.setEmail("email@email.com");
        userService.create(UserMapper.toUserDto(user));
        int userId = userService.getAll().get(0).getId();
        user = UserMapper.toUser(userService.getUserById(userId));

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Request Description");
        itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        ItemRequestDto requestDto = itemRequestService.create(user.getId(),
                ItemRequestMapper.toItemRequestDto(itemRequest));
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, user);

        Item item = new Item();
        item.setOwner(UserMapper.toUser(userService.getUserById(user.getId())));
        item.setAvailable(Boolean.TRUE);
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setRequest(request);

        itemService.create(ItemMapper.toItemDto(item), user.getId());
    }

    @Test
    void createItemRequest() {
        ItemRequestDto dto = itemRequestService.create(user.getId(), itemRequestDto);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(dto.getDescription(), "Request Description")
        );
    }

    @Test
    void findAllByOwner() {
        List<ItemRequestDto> allByOwner = itemRequestService.findAllByOwner(user.getId());

        assertAll(
                () -> assertNotNull(allByOwner),
                () -> assertEquals(1, allByOwner.size())
        );
    }

    @Test
    void getAll() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@user.com");
        UserDto userDto = userService.create(UserMapper.toUserDto(newUser));
        List<ItemRequestDto> all = itemRequestService.findAll(userDto.getId(), PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(all),
                () -> assertEquals(1, all.size())
        );
    }

    @Test
    void getByRequestId() {
        int requestId = itemRequestRepository.findAll().get(0).getId();
        ItemRequestDto requestDto = itemRequestService.findByRequestId(user.getId(), requestId);

        assertAll(
                () -> assertNotNull(requestDto),
                () -> assertEquals(requestId, requestDto.getId()),
                () -> assertEquals(1, requestDto.getItems().size())
        );
    }
}