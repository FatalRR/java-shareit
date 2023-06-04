package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;
    private static final String USERID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemWithBooking> get(@RequestHeader(USERID) Integer userId,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info(String.valueOf(LogMessages.COUNT), itemService.getItems(userId, from, size).size());
        return itemService.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBooking getById(@RequestHeader(USERID) Integer userId,
                                   @PathVariable Integer itemId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getById(@RequestParam(name = "text") String query,
                                 @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                 @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), query);
        return itemService.getByQuery(query, from, size);
    }

    @PostMapping
    public ItemDto save(@RequestHeader(USERID) Integer userId,
                        @RequestBody @Valid ItemDto itemDto) {
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

    @DeleteMapping("/{itemId}")
    public void removeById(@RequestHeader(USERID) Integer userId,
                           @PathVariable Integer itemId) {
        log.info(String.valueOf(LogMessages.TRY_REMOVE), userId);
        itemService.removeById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USERID) Integer userId,
                                 @RequestBody @Valid CommentDto commentDto,
                                 @PathVariable Integer itemId) {
        log.info(String.valueOf(LogMessages.TRY_ADD), commentDto);
        return itemService.addNewComment(userId, commentDto, itemId);
    }
}