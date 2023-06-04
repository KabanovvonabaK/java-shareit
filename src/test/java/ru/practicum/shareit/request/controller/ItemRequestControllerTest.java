package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    private final int userId = 1;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1,
            "Dto Description",
            null,
            null
    );

    private final ItemRequestDto itemRequestDtoResponse = new ItemRequestDto(
            1,
            "Dto Description",
            LocalDateTime.now(),
            Collections.emptyList()
    );

    @Test
    void create() throws Exception {
        when(itemRequestService.create(anyInt(), any()))
                .thenReturn(itemRequestDtoResponse);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDtoResponse.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDtoResponse.getDescription()));

        verify(itemRequestService, times(1)).create(anyInt(), any());
    }

    @Test
    void findAllByOwner() throws Exception {
        when(itemRequestService.findAllByOwner(userId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).findAllByOwner(userId);
    }

    @Test
    void findAll() throws Exception {
        int from = 0;
        int size = 10;
        Sort sort = Sort.by("created").descending();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        when(itemRequestService.findAll(userId, pageRequest))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).findAll(userId, pageRequest);
    }

    @Test
    void findByRequestId() throws Exception {
        int requestId = 1;
        when(itemRequestService.findByRequestId(userId, requestId))
                .thenReturn(itemRequestDtoResponse);

        mockMvc.perform(get("/requests/" + requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDtoResponse.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDtoResponse.getDescription()));

        verify(itemRequestService, times(1)).findByRequestId(userId, requestId);
    }
}