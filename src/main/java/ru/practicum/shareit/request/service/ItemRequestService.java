package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByOwner(int userId);

    List<ItemRequestDto> findAll(int userId, int from, int size);

    ItemRequestDto findByRequestId(int userId, int requestId);
}