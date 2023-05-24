package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    Item save(Integer userId, ItemDto itemDto);

    Item getById(Integer itemId);

    ItemDto update(Integer userId, ItemDto itemDto, Integer itemId);

    List<ItemDto> getByQuery(String query);

    void removeById(Integer userId, Integer itemId);

    Comment addNewComment(Integer userId, Comment comment, Integer itemId);

    ItemWithBooking getItemById(Integer userId, Integer itemId);

    Collection<ItemWithBooking> getItems(Integer userId);
}