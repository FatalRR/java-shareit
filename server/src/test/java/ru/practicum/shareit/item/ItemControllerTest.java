package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    @Test
    void getTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        List<ItemWithBooking> itemWithBookings = List.of(itemWithBooking);
        when(itemService.getItems(1, 1, 10))
                .thenReturn(itemWithBookings);

        List<ItemWithBooking> response = (List<ItemWithBooking>) itemController.get(1, 1, 10);

        assertEquals(itemWithBookings, response);
    }

    @Test
    void getByIdTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        when(itemService.getItemById(1, 1))
                .thenReturn(itemWithBooking);

        ItemWithBooking response = itemController.getById(1, 1);

        assertEquals(itemWithBooking, response);

    }

    @Test
    void getByIdQueryTest() {
        ItemDto itemDto = new ItemDto();
        List<ItemDto> itemDtos = List.of(itemDto);
        when(itemService.getByQuery("good", 1, 10))
                .thenReturn(itemDtos);

        List<ItemDto> response = itemController.getById("good", 1, 10);

        assertEquals(itemDtos, response);
    }

    @Test
    void saveTest() {

        Item item = new Item();
        when(itemService.save(1, ItemMapper.toDto(item)))
                .thenReturn(ItemMapper.toDto(item));

        ItemDto response = itemController.save(1, ItemMapper.toDto(item));

        assertEquals(ItemMapper.toDto(item), response);
    }

    @Test
    void addCommentTest() {
        CommentDto comment = new CommentDto();
        when(itemService.addNewComment(1, comment, 1))
                .thenReturn(comment);

        CommentDto response = itemController.addComment(1, comment, 1);

        assertEquals(comment, response);
    }

    @Test
    void updateTest() {
        ItemDto itemDto = new ItemDto();
        when(itemService.update(1, itemDto, 1))
                .thenReturn(itemDto);

        ItemDto response = itemController.update(1, itemDto, 1);

        assertEquals(itemDto, response);
    }

    @Test
    void removeByIdTest() {
        itemController.removeById(1, 1);

        verify(itemService).removeById(1, 1);
    }
}