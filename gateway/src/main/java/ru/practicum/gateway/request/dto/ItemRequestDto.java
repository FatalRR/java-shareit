package ru.practicum.gateway.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.messages.ExceptionMessages;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;

    @NotBlank(message = ExceptionMessages.EMPTY_DESCRIPTION)
    private String description;
}