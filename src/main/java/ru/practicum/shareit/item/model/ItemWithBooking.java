package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
public class ItemWithBooking {
    private Integer id;

    private String name;

    private String description;

    private User user;

    private Boolean available;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<Comment> comments;
}