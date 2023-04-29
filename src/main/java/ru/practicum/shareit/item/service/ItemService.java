package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(ItemDto itemDto, int userId);

    Item updateItem(int itemId, int userId, ItemDto itemDto);

    Item getByItemId(int itemId);

    List<Item> getByOwnerId(int userId);

    List<Item> search(String text);
}