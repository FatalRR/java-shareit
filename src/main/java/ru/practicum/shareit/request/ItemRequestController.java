package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USERID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto save(@RequestHeader(USERID) Integer userId,
                               @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info(String.valueOf(LogMessages.TRY_ADD), itemRequestDto);
        return itemRequestService.addNewItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getRequest(@RequestHeader(USERID) Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequestAll(@RequestHeader(USERID) Integer userId,
                                              @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info(String.valueOf(LogMessages.COUNT));
        return itemRequestService.getAllRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestId(@RequestHeader(USERID) Integer userId,
                                       @PathVariable Integer requestId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}