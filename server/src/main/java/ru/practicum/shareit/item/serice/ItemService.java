package ru.practicum.shareit.item.serice;

import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, int userId);

    ItemDto updateItem(int itemId, int userId, ItemDto itemDto);

    ItemDto getByItemId(int userId, int itemId);

    List<ItemDto> getByOwnerId(int userId);

    List<ItemDto> getByOwnerId(int userId, int from, int size);

    List<ItemDto> search(String text);

    List<ItemDto> search(int userId, String text, int from, int size);

    CommentDto addComment(int userId, int itemId, CommentRequestDto commentRequestDto);

    List<ItemDto> findAllByRequestIdIn(List<Integer> listRequestIds);

    List<ItemDto> findAllByRequestId(int requestId);
}