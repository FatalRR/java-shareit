package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    public void getItems_ValidUserId_ReturnsItemWithBookingList() {
        Integer userId = 1;
        Integer from = 0;
        Integer size = 10;

        List<Item> items = Arrays.asList(new Item(), new Item());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.findByUserId(userId, PageRequest.of(0, 10, Sort.Direction.ASC, "id"))).thenReturn(new PageImpl<>(items));

        Collection<ItemWithBooking> result = itemService.getItems(userId, from, size);

        assertEquals(items.size(), result.size());
    }

    @Test
    public void getItems_InvalidUserId_ThrowsNotFoundException() {
        Integer userId = 1;
        Integer from = 0;
        Integer size = 10;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItems(userId, from, size));
    }

    @Test
    public void save_ValidUserIdAndItemDto_ReturnsItemDto() {
        Integer userId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);

        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.save(userId, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }

    @Test
    public void save_InvalidUserId_ThrowsNotFoundException() {
        Integer userId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.save(userId, itemDto));
    }

    @Test
    public void getById_ExistingItemId_ReturnsItem() {
        Integer itemId = 1;
        Item item = new Item();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item result = itemService.getById(itemId);

        assertNotNull(result);
        assertEquals(item, result);
    }

    @Test
    public void getById_NonExistingItemId_ThrowsNotFoundException() {
        Integer itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getById(itemId));
    }

    @Test
    public void update_ValidUserIdItemDtoAndItemId_ReturnsUpdatedItemDto() {
        Integer userId = 1;
        Integer itemId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Name");
        itemDto.setDescription("New Description");
        itemDto.setAvailable(true);

        User user = new User();
        Item item = new Item();
        item.setId(itemId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.update(userId, itemDto, itemId);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
    }

    @Test
    public void update_InvalidUserId_ThrowsNotFoundException() {
        Integer userId = 1;
        Integer itemId = 1;
        ItemDto itemDto = new ItemDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.update(userId, itemDto, itemId));
    }

    @Test
    public void getByQuery_ValidQuery_ReturnsItemDtoList() {
        String query = "test";
        Integer from = 0;
        Integer size = 10;
        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Item1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);
        Item item2 = new Item();
        item2.setId(2);
        item2.setName("Item2");
        item2.setDescription("Description 2");
        item2.setAvailable(false);
        Item item3 = new Item();
        item3.setId(3);
        item3.setName("Item3");
        item3.setDescription("Description 3");
        item3.setAvailable(true);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        when(itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(any(), any(), any(Pageable.class)))
                .thenReturn(itemList);

        List<ItemDto> result = itemService.getByQuery(query, from, size);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void getByQuery_InvalidFrom_ThrowsValidationException() {
        String query = "search";
        Integer from = -1;
        Integer size = 10;

        assertThrows(ValidationException.class, () -> itemService.getByQuery(query, from, size));
    }

    @Test
    public void removeById_ValidUserAndItem_RemovesItem() {
        Integer userId = 1;
        Integer itemId = 1;
        User user1 = new User();
        user1.setId(userId);
        user1.setEmail("test1@mail.ru");
        user1.setName("Sergey");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));

        Item item1 = new Item();
        item1.setId(1);
        item1.setName("Item1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);

        itemService.removeById(userId, itemId);

        verify(itemRepository, times(1)).deleteByUserIdAndId(userId, itemId);
    }

    @Test
    public void removeById_InvalidUser_ThrowsException() {
        Integer userId = 1;
        Integer itemId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty()); // Возвращаем Optional.empty() для несуществующего пользователя

        assertThrows(NotFoundException.class, () -> {
            itemService.removeById(userId, itemId);
        });

        verify(itemRepository, never()).deleteByUserIdAndId(anyInt(), anyInt());
    }

    @Test
    public void addNewComment_UserIsBooker_Success() {
        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);

        Comment comment = new Comment();
        comment.setText("Test comment");
        comment.setItem(item);
        comment.setAuthorName(user.getName());
        comment.setCreated(LocalDateTime.now());

        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.addNewComment(user.getId(), CommentMapper.toDto(comment), item.getId());

        assertNotNull(result);
        assertEquals(comment.getText(), result.getText());

        verify(bookingRepository).getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void addNewComment_UserIsNotBooker_ThrowsValidationException() {
        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");


        List<Booking> bookings = Collections.emptyList();

        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class)))
                .thenReturn(bookings);

        assertThrows(ValidationException.class, () -> {
            itemService.addNewComment(user.getId(), new CommentDto(), item.getId());
        });

        verify(bookingRepository).getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class));
    }

    @Test
    public void addNewComment_ItemNotFound_ThrowsNotFoundException() {
        User user = new User();
        user.setId(1);
        user.setName("User1");

        Item item = new Item();
        item.setId(1);
        item.setName("Item1");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);

        List<Booking> bookings = Collections.singletonList(booking);

        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class)))
                .thenReturn(bookings);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.addNewComment(user.getId(), new CommentDto(), item.getId());
        });

        verify(bookingRepository).getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(eq(user.getId()), eq(item.getId()), any(LocalDateTime.class));
        verify(itemRepository).findById(item.getId());
    }
}