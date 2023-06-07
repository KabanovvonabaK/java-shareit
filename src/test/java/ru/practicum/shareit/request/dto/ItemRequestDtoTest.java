package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testJsonItemRequestDto() throws IOException {
        String json = "{\"description\":\"Test json\"}";
        ItemRequestDto itemRequestDto = this.json.parse(json).getObject();

        assertThat(itemRequestDto.getDescription().equals("Test json"));
    }
}