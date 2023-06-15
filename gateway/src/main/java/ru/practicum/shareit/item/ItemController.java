package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemShortDto itemShortDto,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.create(userId, itemShortDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable int itemId,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemShortDto itemShortDto) {
        return itemClient.updateItem(itemId, userId, itemShortDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByItemId(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int itemId) {
        return itemClient.getByItemId(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                      @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemClient.getByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                @RequestHeader("X-Sharer-User-Id") int userId,
                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId,
                                 @Validated(Create.class) @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}