package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testJsonItemDto() throws Exception {
        String jsonContent = "{\"name\":\"Name\", \"description\":\"description\", \"available\":\"true\"}";
        ItemDto itemDto = this.json.parse(jsonContent).getObject();

        assertAll(
                () -> assertEquals("Name", itemDto.getName()),
                () -> assertEquals("description", itemDto.getDescription()),
                () -> assertTrue(itemDto.getAvailable())
        );
    }
}
