package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BookingControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void getByIdTest() {
        BookingDto booking = new BookingDto();
        when(bookingService.getBookingById(1, 1)).thenReturn(booking);

        String response = mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), response);
    }

    @SneakyThrows
    @Test
    void getByUserIdTest() {
        List<BookingDto> bookings = List.of(new BookingDto());
        when(bookingService.getByUserId(1, "ALL", 1, 10)).thenReturn(bookings);

        String response = mockMvc.perform(get("/bookings", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookings), response);
    }

    @SneakyThrows
    @Test
    void getByOwnerIdTest() {
        List<BookingDto> bookings = List.of(new BookingDto());
        when(bookingService.getByOwnerId(1, "ALL", 1, 10)).thenReturn(bookings);

        String response = mockMvc.perform(get("/bookings/owner", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookings), response);
    }

    @SneakyThrows
    @Test
    void approveBookingTest() {
        BookingDto booking = new BookingDto();
        when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(booking);

        String response = mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), response);
    }

    @SneakyThrows
    @Test
    void addTest() {
        BookingDto booking = new BookingDto();
        BookingDto.ItemBooking item = new BookingDto.ItemBooking();
        BookingDto.Booker user = new BookingDto.Booker();
        booking.setItemId(1);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.of(2023, 7, 7, 7, 7));
        booking.setEnd(LocalDateTime.of(2023, 7, 8, 7, 7));
        booking.setId(1);
        when(bookingService.addNewBooking(1, booking)).thenReturn(booking);

        String response = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), response);
    }
}