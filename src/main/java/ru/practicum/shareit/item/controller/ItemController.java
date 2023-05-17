package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.Create;

import java.util.Collections;
import java.util.List;

@RestController
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@Validated(Create.class) @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable int itemId,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getByItemId(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int itemId) {
        return itemService.getByItemId(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId,
                                 @Validated (Create.class) @RequestBody CommentInputDto commentInputDto) {
    return itemService.addComment(userId, itemId, commentInputDto);
    }
}