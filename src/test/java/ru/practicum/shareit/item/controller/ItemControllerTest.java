package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    private final int userId = 1;
    private final int from = 0;
    private final int size = 10;
    private final CommentRequestDto commentRequestDto = new CommentRequestDto("Blah");
    private final CommentDto commentResponseDto = new CommentDto(2, "Comment", null, null);
    private final ItemDto itemDto = new ItemDto(1,
            "Name",
            "Description",
            Boolean.TRUE,
            null,
            null,
            null,
            null,
            null);
    private final ItemDto itemResponseDto = new ItemDto(1,
            "Name",
            "Description",
            Boolean.TRUE,
            null,
            null,
            null,
            null,
            null);

    @Test
    void create() throws Exception {
        when(itemService.create(any(), anyInt()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
        verify(itemService, times(1)).create(any(), anyInt());
    }

    @Test
    void updateItem() throws Exception {
        itemDto.setName("New Name");
        when(itemService.updateItem(anyInt(), anyInt(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/" + userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
        verify(itemService, times(1)).updateItem(anyInt(), anyInt(), any());
    }

    @Test
    void getByItemId() throws Exception {
        when(itemService.getByItemId(anyInt(), anyInt()))
                .thenReturn(itemResponseDto);

        mockMvc.perform(get("/items/" + itemDto.getId())
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
        verify(itemService, times(1)).getByItemId(anyInt(), anyInt());
    }

    @Test
    void getByOwnerId() throws Exception {
        when(itemService.getByOwnerId(userId, from, size))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).getByOwnerId(userId, from, size);
    }

    @Test
    void search() throws Exception {
        String text = "Text";

        when(itemService.search(userId, text, from, size))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).search(userId, text, from, size);
    }

    @Test
    void addComment() throws Exception {
        int itemId = 1;
        when(itemService.addComment(userId, itemId, commentRequestDto))
                .thenReturn(commentResponseDto);

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .content(objectMapper.writeValueAsString(commentRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentResponseDto.getId()));
        verify(itemService, times(1)).addComment(userId, itemId, commentRequestDto);
    }
}