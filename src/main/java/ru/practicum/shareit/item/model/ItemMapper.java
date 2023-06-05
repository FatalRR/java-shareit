package ru.practicum.shareit.item.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public class ItemMapper {
    public static Item toEntity(User user, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setUser(user);
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId());
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(items.spliterator(), false)
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public static ItemWithBooking toEntityWithBooking(Item item, BookingDto lastBooking, BookingDto nextBooking, List<CommentDto> comments) {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        itemWithBooking.setId(item.getId());
        itemWithBooking.setName(item.getName());
        itemWithBooking.setDescription(item.getDescription());
        itemWithBooking.setUserId(item.getUser() != null ? item.getUser().getId() : null);
        itemWithBooking.setAvailable(item.getAvailable());
        itemWithBooking.setLastBooking(lastBooking);
        itemWithBooking.setNextBooking(nextBooking);
        itemWithBooking.setComments(comments);
        itemWithBooking.setRequestId(item.getRequestId());
        return itemWithBooking;
    }
}