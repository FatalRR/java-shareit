package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController requestController;

    @Test
    void getRequestTest() {
        ItemRequestDto itemRequest = new ItemRequestDto();
        Collection<ItemRequestDto> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getRequests(1)).thenReturn(itemRequests);

        Collection<ItemRequestDto> response = requestController.getRequest(1);

        assertEquals(itemRequests, response);
    }

    @Test
    void getRequestIdTest() {
        ItemRequestDto itemRequest = new ItemRequestDto();
        when(itemRequestService.getRequestById(1, 1)).thenReturn(itemRequest);

        ItemRequestDto response = requestController.getRequestId(1, 1);

        assertEquals(itemRequest, response);
    }

    @Test
    void getRequestAllTest() {
        ItemRequestDto itemRequest = new ItemRequestDto();
        List<ItemRequestDto> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getAllRequest(1, 1, 1)).thenReturn(itemRequests);

        List<ItemRequestDto> response = requestController.getRequestAll(1, 1, 1);

        assertEquals(itemRequests, response);
    }

    @Test
    void saveTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        Item item = new Item();
        List<Item> items = List.of(item);
        User user = new User();
        user.setId(1);
        ItemRequest itemRequest = ItemRequestMapper.toEntity(1, itemRequestDto, items);

        when(itemRequestService.addNewItemRequest(1, itemRequestDto)).thenReturn(ItemRequestMapper.toDto(itemRequest));

        ItemRequestDto response = requestController.save(1, itemRequestDto);

        assertEquals(ItemRequestMapper.toDto(itemRequest), response);
    }
}