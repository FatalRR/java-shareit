package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = ExceptionMessages.EMPTY_NAME)
    private String name;

    @NotBlank
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    @Column(name = "email", nullable = false)
    private String email;
}