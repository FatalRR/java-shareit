package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ItemDto {
    Integer id;
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    String name;
    @NotBlank(message = ExceptionMessages.EMPTY_DESCRIPTION)
    String description;
    Boolean available;
}