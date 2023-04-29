package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item, int userId);

    Item updateItem(int itemId, int userId, Item item);

    Item getByItemId(int itemId);

    List<Item> getByOwnerId(int userId);

    List<Item> search(String text);
}