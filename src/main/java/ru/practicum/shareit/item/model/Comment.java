package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Item item;

    @Column
    private String authorName;

    @Column
    private LocalDateTime created;

    @Column
    @NotBlank(message = ExceptionMessages.NOT_EMPTY_TEXT)
    private String text;
}