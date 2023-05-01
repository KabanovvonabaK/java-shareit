package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemInMemoryRepository implements ItemRepository {

    private final HashMap<Integer, Item> items = new HashMap<>();
    private int id = 1;

    @Override
    public Item create(Item item, int userId) {
        item.setId(id++);
        item.setOwner(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(int itemId, int userId, Item item) {
        items.put(itemId, item);
        return getByItemId(itemId);
    }

    @Override
    public Item getByItemId(int itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getByOwnerId(int userId) {
        return items.values().stream().filter(item -> item.getOwner() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return items
                .values()
                .stream()
                .filter(item ->
                        (item.getName().toLowerCase().contains(text.toLowerCase()) && item.getAvailable())
                        || (item.getDescription().toLowerCase().contains(text.toLowerCase()) && item.getAvailable()))
                .collect(Collectors.toList());
    }

    public void checkItemExist(int itemId) {
        if (!items.containsKey(itemId)) {
            throw new EntityNotFoundException(String.format("Item with id %s not exist", itemId));
        }
    }
}