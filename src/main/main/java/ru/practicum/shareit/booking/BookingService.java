package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto getBookingById(Integer userId, Integer bookingId);

    List<BookingDto> getByUserId(Integer userId, String state, Integer from, Integer size);

    List<BookingDto> getByOwnerId(Integer userId, String state, Integer from, Integer size);

    BookingDto approveBooking(Integer userId, Integer bookingId, boolean approve);

    BookingDto addNewBooking(Integer userId, BookingDto bookingDto);
}