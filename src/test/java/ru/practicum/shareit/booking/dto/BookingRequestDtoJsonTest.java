package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class BookingRequestDtoJsonTest {
    @Autowired
    private JacksonTester<BookingRequestDto> json;

    @Test
    void testJsonBookingDto() throws Exception {
        String jsonContent = "{\"itemId\":\"1\", \"start\":\"2023-06-05T12:00:00\", \"end\":\"2023-06-06T13:00:00\"}";
        BookingRequestDto bookingRequestDto = this.json.parse(jsonContent).getObject();

        assertAll(
                () -> assertEquals(1, bookingRequestDto.getItemId()),
                () -> assertEquals(LocalDateTime.of(2023, 6, 5, 12, 0, 0),
                        bookingRequestDto.getStart()),
                () -> assertEquals(LocalDateTime.of(2023, 6, 6, 13, 0, 0),
                        bookingRequestDto.getEnd())
        );
    }
}
