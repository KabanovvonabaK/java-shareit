package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private int id;
    @NotBlank(groups = {Create.class}, message = "Description is mandatory")
    private String description;
    private LocalDateTime created;
    // TODO recheck
//    private List<ItemDto> items;
}