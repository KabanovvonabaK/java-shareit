package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;

    private final ItemService itemService;

    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDto create(int userId, ItemRequestDto itemRequestDto) {
        log.info("Attempt to create item request {}", itemRequestDto);
        User user = UserMapper.toUser(userService.getUserById(userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findAllByOwner(int userId) {
        log.info("Attempt to get item requests by owner with id {}", userId);
        userService.getUserById(userId);
        Sort sort = Sort.by("created").descending();
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository.findAllByRequester_Id(userId, sort)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        return setItemsToRequest(itemRequestDtoList);
    }

    @Override
    public List<ItemRequestDto> findAll(int userId, int from, int size) {
        log.info("Attempt to find all item requests by user id {}", userId);
        Sort sort = Sort.by("created").descending();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        userService.getUserById(userId);
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository
                .findAllByRequester_IdNot(userId, pageRequest)
                .map(ItemRequestMapper::toItemRequestDto)
                .getContent();
        return setItemsToRequest(itemRequestDtoList);
    }

    @Override
    public ItemRequestDto findByRequestId(int userId, int requestId) {
        log.info("Attempt to find request by id {} via user with id {}", requestId, userId);
        userService.getUserById(userId);
        ItemRequestDto itemRequestDto = ItemRequestMapper
                .toItemRequestDto(itemRequestRepository
                        .findById(requestId)
                        .orElseThrow(() -> new EntityNotFoundException("No request with such id")));
        List<ItemDto> itemDtos = itemService.findAllByRequestId(requestId);
        itemRequestDto.setItems(itemDtos);
        return itemRequestDto;
    }

    private List<ItemRequestDto> setItemsToRequest(List<ItemRequestDto> itemRequestDtoList) {
        List<Integer> requestIds = itemRequestDtoList
                .stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList());
        List<ItemDto> itemDtos = itemService.findAllByRequestIdIn(requestIds);
        itemRequestDtoList
                .forEach(r -> r.setItems(itemDtos
                        .stream()
                        .filter(itemDto -> itemDto.getRequestId() == r.getId())
                        .collect(Collectors.toList())));

        return itemRequestDtoList;
    }
}