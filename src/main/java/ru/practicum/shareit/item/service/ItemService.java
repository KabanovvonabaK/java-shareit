package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, int userId);

    ItemDto updateItem(int itemId, int userId, ItemDto itemDto);

    ItemDto getByItemId(int itemId);

    List<ItemDto> getByOwnerId(int userId);

    List<ItemDto> search(String text);
}