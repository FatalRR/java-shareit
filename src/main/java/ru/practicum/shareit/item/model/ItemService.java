package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item save(Integer userId, ItemDto itemDto);

    Collection<Item> getByUserId(Integer userId);

    ItemDto getById(Integer itemId);

    ItemDto update(Integer userId, ItemDto itemDto, Integer itemId);

    List<ItemDto> getByQuery(String query);

    void removeById(Integer userId, Integer itemId);
}