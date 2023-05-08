package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private Integer id;
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;
    @NotBlank
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;
}