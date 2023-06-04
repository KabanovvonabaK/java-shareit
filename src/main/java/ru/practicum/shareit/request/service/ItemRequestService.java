package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByOwner(int userId);

    List<ItemRequestDto> findAll(int userId, PageRequest pageRequest);

    ItemRequestDto findByRequestId(int userId, int requestId);
}