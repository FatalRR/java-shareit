package ru.practicum.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.messages.LogMessages;
import ru.practicum.gateway.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    private static final String USERID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(USERID) Integer userId,
                                       @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info(String.valueOf(LogMessages.TRY_ADD), itemRequestDto);
        return itemRequestClient.addNewItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestId(@NotNull @RequestHeader(USERID) Integer userId,
                                               @PathVariable Integer requestId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestAll(@NotNull @RequestHeader(USERID) Integer userId,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return itemRequestClient.getAllRequest(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getRequest(@RequestHeader(USERID)
                                             Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return itemRequestClient.getRequests(userId);
    }
}