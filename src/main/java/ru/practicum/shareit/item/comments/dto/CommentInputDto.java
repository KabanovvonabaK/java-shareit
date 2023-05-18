package ru.practicum.shareit.item.comments.dto;

import lombok.Data;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;

@Data
public class CommentInputDto {
    @NotBlank(groups = {Create.class}, message = "Text is mandatory")
    private String text;
}