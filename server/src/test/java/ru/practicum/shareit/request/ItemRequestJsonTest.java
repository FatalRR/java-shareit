package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jsonItemRequest;

    @Test
    void testItemRequestDto() throws IOException {
        LocalDateTime created = LocalDateTime.of(2022, 2, 2, 2, 2, 2);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("description");
        itemRequestDto.setItems(new ArrayList<>());
        itemRequestDto.setCreated(created);

        JsonContent<ItemRequestDto> result = jsonItemRequest.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(new ArrayList<>());
    }
}