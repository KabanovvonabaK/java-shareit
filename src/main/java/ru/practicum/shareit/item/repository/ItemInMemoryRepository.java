package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.errorHandler.exception.EntityNotFoundException;
import ru.practicum.shareit.errorHandler.exception.FieldNotFoundException;
import ru.practicum.shareit.errorHandler.exception.UserIsNotAnOwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemInMemoryRepository implements ItemRepository {

    List<Item> items = new ArrayList<>();
    private int id = 1;

    @Override
    public Item create(Item item, int userId) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new FieldNotFoundException("Name is mandatory.");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new FieldNotFoundException("Description is mandatory.");
        }
        if (item.getAvailable() == null) {
            throw new FieldNotFoundException("Available is mandatory.");
        }
        item.setId(id++);
        item.setOwner(userId);
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(int itemId, int userId, Item item) {
        checkItemExist(itemId);
        Item oldItem = getByItemId(itemId);
        if (oldItem.getOwner() == userId) {
            if (item.getName() != null) {
                oldItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                oldItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
            }
        } else {
            throw new UserIsNotAnOwnerException(
                    String.format("User with id %s is not an owner of item with id %s", userId, itemId));
        }
        return getByItemId(itemId);
    }

    @Override
    public Item getByItemId(int itemId) {
        checkItemExist(itemId);
        Item item = new Item();
        for (Item i : items) {
            if (i.getId() == itemId) {
                item = i;
            }
        }
        return item;
    }

    @Override
    public List<Item> getByOwnerId(int userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item i : items) {
            if (i.getOwner() == userId) {
                itemList.add(i);
            }
        }
        return itemList;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemList = new ArrayList<>();
        if (text.length() > 0) {
            for (Item i : items) {
                if (i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    if (i.getAvailable().equals(true)) {
                        itemList.add(i);
                    }
                }
            }
        }
        return itemList;
    }

    private void checkItemExist(int itemId) {
        List<Integer> ids = new ArrayList<>();
        for (Item i : items) {
            ids.add(i.getId());
        }
        if (!ids.contains(itemId)) {
            throw new EntityNotFoundException(String.format("Item with id %s not exist", itemId));
        }
    }
}