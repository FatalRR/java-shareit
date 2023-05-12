package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    List<Item> getByUserId(Integer userId);

    Item getById(Integer id);

    Item save(Item item);

    Item update(Item item);

    void removeByUserIdAndItemId(Integer userId, Integer itemId);

    List<ItemDto> getItemByQuery(String query);
}