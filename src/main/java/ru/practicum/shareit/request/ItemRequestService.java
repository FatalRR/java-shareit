package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(Integer userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> getRequests(Integer userId);

    List<ItemRequestDto> getAllRequest(Integer userId, Integer form, Integer size);

    ItemRequestDto getRequestById(Integer userId, Integer requestId);
}