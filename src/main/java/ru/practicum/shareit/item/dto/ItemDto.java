package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;
    @NotBlank(message = ExceptionMessages.EMPTY_DESCRIPTION)
    private String description;
    private Boolean available;
    private Integer requestId;
}