package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ItemControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void getTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        List<ItemWithBooking> itemWithBookings = List.of(itemWithBooking);
        when(itemService.getItems(1, 1, 10)).thenReturn(itemWithBookings);

        String response = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemWithBookings), response);
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        when(itemService.getItemById(1, 1)).thenReturn(itemWithBooking);

        String response = mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemWithBooking), response);
    }

    @SneakyThrows
    @Test
    void getByIdQueryTest() {
        ItemDto itemDto = new ItemDto();
        List<ItemDto> itemDtos = List.of(itemDto);
        when(itemService.getByQuery("good", 1, 10)).thenReturn(itemDtos);

        String response = mockMvc.perform(get("/items//search", 1L)
                        .param("text", "good")
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtos), response);
    }

    @SneakyThrows
    @Test
    void addTest() {
        Item item = new Item();
        item.setName("i");
        item.setDescription("I");
        item.setAvailable(true);
        when(itemService.save(1, ItemMapper.toDto(item))).thenReturn(ItemMapper.toDto(item));

        String response = mockMvc.perform(post("/items", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ItemMapper.toDto(item))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(ItemMapper.toDto(item)), response);
    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionName() {
        Item item = new Item();
        item.setName(null);
        item.setDescription("I");
        item.setAvailable(true);
        when(itemService.save(1, ItemMapper.toDto(item))).thenReturn(ItemMapper.toDto(item));

        mockMvc.perform(post("/items", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ItemMapper.toDto(item))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).save(anyInt(), any(ItemDto.class));

    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionDescription() {
        Item item = new Item();
        item.setName("i");
        item.setDescription(null);
        item.setAvailable(true);
        when(itemService.save(1, ItemMapper.toDto(item))).thenReturn(ItemMapper.toDto(item));

        mockMvc.perform(post("/items", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ItemMapper.toDto(item))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).save(anyInt(), any(ItemDto.class));

    }

    @SneakyThrows
    @Test
    void addCommentTest() {
        CommentDto comment = new CommentDto();
        comment.setText("text");
        when(itemService.addNewComment(1, comment, 1)).thenReturn(comment);

        String response = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(comment), response);
    }

    @SneakyThrows
    @Test
    void addCommentTestShouldThrowException() {
        CommentDto comment = new CommentDto();
        comment.setText(null);
        when(itemService.addNewComment(1, comment, 1)).thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).addNewComment(anyInt(), any(CommentDto.class), anyInt());
    }

    @SneakyThrows
    @Test
    void updateTest() {
        ItemDto itemDto = new ItemDto();
        when(itemService.update(1, itemDto, 1)).thenReturn(itemDto);

        String response = mockMvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), response);
    }

    @SneakyThrows
    @Test
    void removeByIdTest() {
        mockMvc.perform(delete("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemService).removeById(1, 1);
    }
}