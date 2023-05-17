package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemShortDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;

    public ItemShortDto(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemShortDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
