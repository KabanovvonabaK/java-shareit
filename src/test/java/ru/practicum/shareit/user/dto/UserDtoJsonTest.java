package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testJsonUserDto() throws Exception {
        String jsonContent = "{\"name\":\"Name\", \"email\":\"email@email.com\"}";
        UserDto userDto = this.json.parse(jsonContent).getObject();

        assertAll(
                () -> assertEquals("Name", userDto.getName()),
                () -> assertEquals("email@email.com", userDto.getEmail())
        );
    }
}
