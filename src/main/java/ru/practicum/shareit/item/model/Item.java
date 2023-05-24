package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    private User user;

    @Column
    private Boolean available;
}