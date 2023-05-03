package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemInMemoryRepository implements ItemRepository {

    private final Map<Integer, Item> items = new HashMap<>();
    private int id = 1;

    @Override
    public Item create(Item item, int userId) {
        item.setId(id++);
        item.setOwner(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getByItemId(int itemId) {
        checkItemExist(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getByOwnerId(int userId) {
        return items.values().stream().filter(item -> item.getOwner() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        return items
                .values()
                .stream()
                .filter(item -> item.getAvailable() &&
                        ((item.getName().toLowerCase().contains(text.toLowerCase()))
                                || (item.getDescription().toLowerCase().contains(text.toLowerCase()))))
                .collect(Collectors.toList());
    }

    private void checkItemExist(int itemId) {
        if (!items.containsKey(itemId)) {
            throw new EntityNotFoundException(String.format("Item with id %s not exist", itemId));
        }
    }
}