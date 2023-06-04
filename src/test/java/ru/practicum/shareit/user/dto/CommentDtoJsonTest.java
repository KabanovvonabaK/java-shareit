package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.comments.dto.CommentDto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testJsonCommentDto() throws Exception {
        String jsonContent = "{\"text\":\"text\"}";
        CommentDto commentDto = this.json.parse(jsonContent).getObject();

        assertAll(
                () -> assertEquals("text", commentDto.getText())
        );
    }
}
