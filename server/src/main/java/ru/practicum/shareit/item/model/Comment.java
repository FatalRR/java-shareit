package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
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
    private String text;
}