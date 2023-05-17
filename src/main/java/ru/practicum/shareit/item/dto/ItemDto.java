package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
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

    public ItemDto(String name, String description, Boolean available, UserShortDto owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public ItemDto(int id, String name, String description, Boolean available, UserShortDto owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}