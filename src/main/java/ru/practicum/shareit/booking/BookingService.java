package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking getBookingById(Integer userId, Integer bookingId);

    List<Booking> getByUserId(Integer userId, String state);

    List<Booking> getByOwnerId(Integer userId, String state);

    Booking approveBooking(Integer userId, Integer bookingId, boolean approve);

    Booking addNewBooking(Integer userId, BookingDto bookingDto);
}