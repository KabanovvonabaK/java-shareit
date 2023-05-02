package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.errorHandler.exception.UserIsNotAnOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto create(ItemDto itemDto, int userId) {
        log.info("Attempt to create item {}", itemDto);
        userService.checkUserExist(userId);
        return ItemMapper.toItemDto(itemRepository.create(ItemMapper.toItem(itemDto), userId));
    }

    @Override
    public ItemDto updateItem(int itemId, int userId, ItemDto itemDto) {
        log.info("Attempt to update item {}", itemDto);
        userService.checkUserExist(userId);
        Item item = itemRepository.getByItemId(itemId);
        if (item.getOwner() == userId) {
            if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
        } else {
            throw new UserIsNotAnOwnerException(
                    String.format("User with id %s is not an owner of item with id %s", userId, itemId));
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getByItemId(int itemId) {
        log.info("Attempt to get item by itemId {}", itemId);
        return ItemMapper.toItemDto(itemRepository.getByItemId(itemId));
    }

    @Override
    public List<ItemDto> getByOwnerId(int userId) {
        log.info("Attempt to get item by userId {}", userId);
        userService.checkUserExist(userId);
        return itemRepository
                .getByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        log.info("Attempt to search by \"{}\"", text);
        return itemRepository
                .search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}