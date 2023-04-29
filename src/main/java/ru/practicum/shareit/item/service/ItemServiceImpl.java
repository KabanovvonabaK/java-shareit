package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public Item create(ItemDto itemDto, int userId) {
        log.info("Attempt to create item {}", itemDto);
        userService.checkUserExist(userId);
        return itemRepository.create(ItemMapper.toItem(itemDto), userId);
    }

    @Override
    public Item updateItem(int itemId, int userId, ItemDto itemDto) {
        log.info("Attempt to update item {}", itemDto);
        userService.checkUserExist(userId);
        return itemRepository.updateItem(itemId, userId, ItemMapper.toItem(itemDto));
    }

    @Override
    public Item getByItemId(int itemId) {
        log.info("Attempt to get item by itemId {}", itemId);
        return itemRepository.getByItemId(itemId);
    }

    @Override
    public List<Item> getByOwnerId(int userId) {
        log.info("Attempt to get item by userId {}", userId);
        userService.checkUserExist(userId);
        return itemRepository.getByOwnerId(userId);
    }

    @Override
    public List<Item> search(String text) {
        log.info("Attempt to search by \"{}\"", text);
        return itemRepository.search(text);
    }
}