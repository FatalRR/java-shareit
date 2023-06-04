package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;

    private Integer itemId;

    private String authorName;

    private LocalDateTime created;

    @NotBlank(message = ExceptionMessages.NOT_EMPTY_TEXT)
    private String text;
}