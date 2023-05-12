package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private static final String USERID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<Item> getAll(@RequestHeader(USERID) Integer userId) {
        log.info(String.valueOf(LogMessages.COUNT), itemService.getByUserId(userId).size());
        return itemService.getByUserId(userId);
    }

    @PostMapping
    public Item save(@RequestHeader(USERID) Integer userId,
                     @RequestBody @Valid ItemDto itemDto) throws ValidationException {
        log.info(String.valueOf(LogMessages.TRY_ADD), itemDto);
        return itemService.save(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USERID) Integer userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Integer itemId) {
        log.info(String.valueOf(LogMessages.TRY_UPDATE), itemDto);
        return itemService.update(userId, itemDto, itemId);
    }

    @GetMapping("/{userId}")
    public ItemDto getById(@PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return itemService.getById(userId);
    }

    @DeleteMapping("/{itemId}")
    public void removeById(@RequestHeader(USERID) Integer userId,
                           @PathVariable Integer itemId) {
        log.info(String.valueOf(LogMessages.TRY_REMOVE), userId);
        itemService.removeById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getById(@RequestParam(name = "text") String query) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), query);
        return itemService.getByQuery(query);
    }
}