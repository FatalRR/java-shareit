package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ItemRequestControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void getRequestTest() {
        ItemRequestDto itemRequest = new ItemRequestDto();
        List<ItemRequestDto> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getRequests(1)).thenReturn(itemRequests);

        String response = mockMvc.perform(get("/requests", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequests), response);
    }

    @SneakyThrows
    @Test
    void getRequestIdTest() {
        ItemRequestDto itemRequest = new ItemRequestDto();
        when(itemRequestService.getRequestById(1, 1)).thenReturn(itemRequest);

        String response = mockMvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequest), response);
    }

    @SneakyThrows
    @Test
    void getRequestAllTest() {
        ItemRequestDto itemRequest = new ItemRequestDto();
        List<ItemRequestDto> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getAllRequest(1, 1, 10)).thenReturn(itemRequests);

        String response = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequests), response);
    }

    @SneakyThrows
    @Test
    void saveTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("text");
        Item item = new Item();
        List<Item> items = List.of(item);
        User user = new User();
        user.setId(1);
        ItemRequest itemRequest = ItemRequestMapper.toEntity(1, itemRequestDto, items);

        when(itemRequestService.addNewItemRequest(1, itemRequestDto)).thenReturn(ItemRequestMapper.toDto(itemRequest));

        String response = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(ItemRequestMapper.toDto(itemRequest)), response);
    }

}