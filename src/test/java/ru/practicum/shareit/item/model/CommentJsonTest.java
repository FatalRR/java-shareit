package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentJsonTest {
    @Autowired
    private JacksonTester<CommentDto> jsonCommentDto;

    @Test
    void testCommentDto() throws IOException {
        LocalDateTime created = LocalDateTime.of(2022, 2, 2, 2, 2, 2);
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setItemId(1);
        commentDto.setCreated(created);
        commentDto.setAuthorName("name");

        JsonContent<CommentDto> result = jsonCommentDto.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("name");
    }
}