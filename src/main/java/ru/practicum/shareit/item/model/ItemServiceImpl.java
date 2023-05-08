package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    ItemMapper itemMapper = new ItemMapper();

    @Override
    public Item save(Integer userId, ItemDto itemDto) throws ValidationException {
        userRepository.getById(userId);
        Item item = itemMapper.toEntity(userId, itemDto);
        if (item.getAvailable() == null || !item.getAvailable()) {
            throw new ValidationException(ExceptionMessages.ITEM_BE_AVAILABLE);
        }
        return itemRepository.save(item);
    }

    @Override
    public Collection<Item> getByUserId(Integer userId) {
        userRepository.getById(userId);
        return itemRepository.getByUserId(userId);
    }

    @Override
    public ItemDto getById(Integer itemId) {
        return itemMapper.toDto(itemRepository.getById(itemId));
    }

    @Override
    public ItemDto update(Integer userId, ItemDto itemDto, Integer itemId) {
        userRepository.getById(userId);
        itemDto.setId(itemId);
        Item item = itemMapper.toEntity(userId, itemDto);
        validate(userId, itemRepository.getById(itemDto.getId()));
        return itemMapper.toDto(itemRepository.update(item));
    }

    @Override
    public List<ItemDto> getByQuery(String query) {
        if (query.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.getItemByQuery(query);
    }

    @Override
    public void removeById(Integer userId, Integer itemId) {
        userRepository.getById(userId);
        validate(userId, itemRepository.getById(itemId));
        itemRepository.removeByUserIdAndItemId(userId, itemId);
    }

    private void validate(Integer userId, Item item) {
        if (!Objects.equals(userId, item.getOwnerId())) {
            throw new NotFoundException(ExceptionMessages.NOT_OWNER);
        }
    }
}