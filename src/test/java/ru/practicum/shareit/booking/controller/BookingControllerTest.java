package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    private final int userId = 1;
    private final int from = 0;
    private final int size = 10;

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            1
    );

    private final BookingDto bookingDto = new BookingDto(
            1,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            new ItemShortDto(1, "Name", "Description", Boolean.TRUE),
            new UserShortDto(1, "Name"),
            BookingStatus.WAITING
    );

    private final BookingDto bookingDtoApproved = new BookingDto(
            1,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            new ItemShortDto(1, "Name", "Description", Boolean.TRUE),
            new UserShortDto(1, "Name"),
            BookingStatus.APPROVED
    );

    @Test
    void getById() throws Exception {
        when(bookingService.getById(anyInt(), anyInt()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/" + bookingDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value(BookingStatus.WAITING.name()));
        verify(bookingService, times(1)).getById(anyInt(), anyInt());
    }

    @Test
    void getByBookerId() throws Exception {
        String state = "FUTURE";
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);

        when(bookingService.getByBookerId(userId, state, pageRequest))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).getByBookerId(userId, state, pageRequest);
    }

    @Test
    void getByOwnerId() throws Exception {
        String state = "FUTURE";
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);

        when(bookingService.getByOwnerId(userId, state, pageRequest))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).getByOwnerId(userId, state, pageRequest);
    }

    @Test
    void create() throws Exception {
        when(bookingService.create(anyInt(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().name()));
        verify(bookingService, times(1)).create(anyInt(), any());
    }

    @Test
    void approve() throws Exception {
        when(bookingService.approve(anyInt(), anyInt(), any()))
                .thenReturn(bookingDtoApproved);

        mockMvc.perform(patch("/bookings/" + bookingDto.getId())
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("approved", String.valueOf(Boolean.TRUE))
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(bookingDtoApproved.getStatus().name()));
        verify(bookingService, times(1)).approve(anyInt(), anyInt(), any());
    }
}