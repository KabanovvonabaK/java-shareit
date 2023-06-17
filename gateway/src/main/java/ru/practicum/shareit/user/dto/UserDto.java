package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotBlank(groups = {Create.class}, message = "Name is mandatory")
    private String name;
    @Email(groups = {Create.class, Update.class}, message = "Email is incorrect")
    @NotBlank(groups = {Create.class}, message = "Email is mandatory")
    private String email;
}