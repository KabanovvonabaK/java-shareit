package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    @NotBlank(groups = {Create.class}, message = "Name is mandatory")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Description is mandatory")
    private String description;
    @NotNull(groups = {Create.class}, message = "Available is mandatory")
    private Boolean available;
}