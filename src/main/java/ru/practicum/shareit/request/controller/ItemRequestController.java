package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.findAllByOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") int userId,
                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                        @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findByRequestId(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable int requestId) {
        return itemRequestService.findByRequestId(userId, requestId);
    }
}