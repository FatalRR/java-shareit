package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
public class ItemWithBooking {
    Integer id;

    String name;

    String description;

    User user;

    Boolean available;

    BookingDto lastBooking;

    BookingDto nextBooking;

    List<Comment> comments;
}