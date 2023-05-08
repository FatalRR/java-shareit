package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, List<Item>> items = new HashMap<>();
    private Integer id = 1;
    ItemMapper itemMapper = new ItemMapper();

    private Integer generateId() {
        return id++;
    }

    @Override
    public List<Item> getByUserId(Integer userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item getById(Integer id) {
        for (List<Item> userItems: items.values()) {
            for (Item item: userItems) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }
        throw new NotFoundException(String.format("Вещь с ID =%d не найдена", id));
    }

    @Override
    public Item save(final Item item) {
        item.setId(generateId());
        items.compute(item.getOwnerId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(Item item) {
        items.compute(item.getOwnerId(), (userId, userItems) -> {
            int index = -1;
            for (Item itemTemp : userItems) {
                if (itemTemp.getId().equals(item.getId())) {
                    index = userItems.indexOf(itemTemp);
                    if (item.getName() == null) {
                        item.setName(itemTemp.getName());
                    }
                    if (item.getDescription() == null) {
                        item.setDescription(itemTemp.getDescription());
                    }
                    if (item.getAvailable() == null) {
                        item.setAvailable(itemTemp.getAvailable());
                    }
                }
            }
            userItems.set(index, item);
            return userItems;
        });
        return item;
    }

    @Override
    public void removeByUserIdAndItemId(Integer userId, Integer itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    @Override
    public List<ItemDto> getItemByQuery(String query) {
        List<ItemDto> itemsByQuery = new ArrayList<>();
        for (List<Item> userItems: items.values()) {
            for (Item item: userItems) {
                if (item.getAvailable() && (item.getName().toUpperCase().contains(query.toUpperCase()) ||
                        item.getDescription().toUpperCase().contains(query.toUpperCase()))) {
                    itemsByQuery.add(itemMapper.toDto(item));
                }
            }
        }
        return itemsByQuery;
    }
}