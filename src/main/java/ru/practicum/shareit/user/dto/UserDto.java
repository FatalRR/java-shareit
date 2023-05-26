package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;
    @NotBlank
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;
}