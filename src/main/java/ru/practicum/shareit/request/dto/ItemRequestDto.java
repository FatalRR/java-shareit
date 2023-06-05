package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;

    @NotBlank(message = ExceptionMessages.EMPTY_DESCRIPTION)
    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}