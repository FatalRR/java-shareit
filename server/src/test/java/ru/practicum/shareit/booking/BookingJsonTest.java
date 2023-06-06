package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingJsonTest {
    @Autowired
    private JacksonTester<BookingDto> jsonBookingDto;

    @Test
    void testBookingDto() throws IOException {
        Integer itemId = 1;
        Integer userId = 1;
        BookingDto.Booker booker = new BookingDto.Booker();
        booker.setId(userId);
        booker.setName("booker");
        BookingDto.ItemBooking item = new BookingDto.ItemBooking();
        item.setId(itemId);
        item.setName("item");
        LocalDateTime start = LocalDateTime.of(2022, 2, 2, 2, 2, 2);
        LocalDateTime end = LocalDateTime.of(2023, 3, 3, 3, 3, 3);
        BookingDto booking = new BookingDto();
        booking.setId(1);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItemId(itemId);
        booking.setBookerId(userId);
        booking.setBooker(booker);
        booking.setItem(item);

        JsonContent<BookingDto> result = jsonBookingDto.write(booking);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(userId);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("booker");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(itemId);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
    }
}