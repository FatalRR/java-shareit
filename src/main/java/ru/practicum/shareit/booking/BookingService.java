package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {
    Booking getBookingById(Integer userId, Integer bookingId);

    List<Booking> getByUserId(Integer userId, String state);

    List<Booking> getByOwnerId(Integer userId, String state);

    @Transactional
    Booking approveBooking(Integer userId, Integer bookingId, boolean approve);

    @Transactional
    Booking addNewBooking(Integer userId, BookingDto bookingDto);
}