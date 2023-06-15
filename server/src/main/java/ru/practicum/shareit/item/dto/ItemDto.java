package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private int id;
    @NotBlank(groups = {Create.class}, message = "Name is mandatory")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Description is mandatory")
    private String description;
    @NotNull(groups = {Create.class}, message = "Available is mandatory")
    private Boolean available;
    private UserShortDto owner;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;
    private Integer requestId;

    public ItemDto(int id, String name, String description, Boolean available, UserShortDto owner, Integer requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
    }
}