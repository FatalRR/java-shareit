package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto getBookingById(Integer userId, Integer bookingId);

    List<BookingDto> getByUserId(Integer userId, String state);

    List<BookingDto> getByOwnerId(Integer userId, String state);

    BookingDto approveBooking(Integer userId, Integer bookingId, boolean approve);

    BookingDto addNewBooking(Integer userId, BookingDto bookingDto);
}