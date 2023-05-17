package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingInputDto {
    @Future
    @NotNull(groups = {Create.class}, message = "Start is mandatory")
    private LocalDateTime start;
    @Future
    @NotNull(groups = {Create.class}, message = "End is mandatory")
    private LocalDateTime end;
    private int itemId;
}