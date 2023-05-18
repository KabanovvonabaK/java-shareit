package ru.practicum.shareit.item.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}