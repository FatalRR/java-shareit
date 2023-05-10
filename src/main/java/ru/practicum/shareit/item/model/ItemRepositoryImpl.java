package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.ExceptionMessages;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, List<Item>> items = new HashMap<>();
    private final Map<Integer, Item> itemMap = new HashMap<>();
    private Integer id = 1;

    private Integer generateId() {
        return id++;
    }

    @Override
    public List<Item> getByUserId(Integer userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item getById(Integer id) {
        Item item = itemMap.get(id);
        if (item == null) {
            throw new NotFoundException(String.format(ExceptionMessages.NOT_ITEM, id));
        }
        return item;
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
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item existingItem = itemMap.get(item.getId());
        if (existingItem == null) {
            throw new NotFoundException(String.format(ExceptionMessages.NOT_ITEM, item.getId()));
        }
        if (item.getName() == null) {
            item.setName(existingItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(existingItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(existingItem.getAvailable());
        }
        items.compute(item.getOwnerId(), (userId, userItems) -> {
            int index = userItems.indexOf(existingItem);
            userItems.set(index, item);
            return userItems;
        });
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public void removeByUserIdAndItemId(Integer userId, Integer itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
        itemMap.remove(itemId);
    }

    @Override
    public List<ItemDto> getItemByQuery(String query) {
        List<ItemDto> itemsByQuery = new ArrayList<>();
        for (List<Item> userItems : items.values()) {
            for (Item item : userItems) {
                if (item.getAvailable() && (item.getName().toUpperCase().contains(query.toUpperCase()) ||
                        item.getDescription().toUpperCase().contains(query.toUpperCase()))) {
                    itemsByQuery.add(ItemMapper.toDto(item));
                }
            }
        }
        return itemsByQuery;
    }
}