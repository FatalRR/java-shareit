package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

@AllArgsConstructor
public class ItemMapper {
    public static Item toEntity(Integer ownerId, ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                ownerId,
                itemDto.getAvailable());
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }
}