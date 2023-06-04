package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    @Mock
    private final ItemRequestRepository itemRequestRepository;

    @Mock
    private final UserService userService;

    @Mock
    private final ItemService itemService;
    private ItemRequestService itemRequestService;
    private ItemRequestDto itemRequestDto;
    private User user;

    @BeforeEach
    public void setUp() {
        itemRequestService = new ItemRequestServiceImpl(userService, itemService, itemRequestRepository);
        user = new User();
        user.setId(1);
        user.setName("Name");
        user.setEmail("email@email.com");
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Description");

        when(userService.getUserById(anyInt()))
                .thenReturn(UserMapper.toUserDto(user));
        when(itemRequestRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void create() {
        ItemRequestDto dto = itemRequestService.create(1, itemRequestDto);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(itemRequestDto.getDescription(), dto.getDescription()),
                () -> verify(userService, times(1)).getUserById(1),
                () -> verify(itemRequestRepository, times(1)).save(any())
        );
    }

    @Test
    void findAllByOwner() {
        when(itemRequestRepository.findAllByRequester_Id(anyInt(), any()))
                .thenReturn(Collections.emptyList());

        List<ItemRequestDto> allByOwner = itemRequestService.findAllByOwner(1);

        assertAll(
                () -> assertNotNull(allByOwner),
                () -> assertTrue(allByOwner.isEmpty()),
                () -> verify(itemRequestRepository, times(1))
                        .findAllByRequester_Id(anyInt(), any()),
                () -> verify(userService, times(1)).getUserById(1)
        );
    }

    @Test
    void findAll() {
        when(itemRequestRepository.findAllByRequester_IdNot(anyInt(), any()))
                .thenReturn(Page.empty());

        List<ItemRequestDto> all = itemRequestService.findAll(1, PageRequest.of(0, 10));

        assertAll(
                () -> assertNotNull(all),
                () -> assertTrue(all.isEmpty()),
                () -> verify(itemRequestRepository, times(1)).findAllByRequester_IdNot(anyInt(), any()),
                () -> verify(userService, times(1)).getUserById(1)
        );
    }

    @Test
    void findByRequestId() {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);

        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.of(itemRequest));
        when(itemService.findAllByRequestId(anyInt()))
                .thenReturn(Collections.emptyList());

        ItemRequestDto byRequestId = itemRequestService.findByRequestId(1, 1);

        assertAll(
                () -> assertNotNull(byRequestId),
                () -> verify(itemRequestRepository, times(1)).findById(1),
                () -> verify(itemService, times(1)).findAllByRequestId(1),
                () -> verify(userService, times(1)).getUserById(1)
        );
    }
}