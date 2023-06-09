package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.messages.LogMessages;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private static final String USERID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(USERID) Integer userId,
                              @PathVariable Integer bookingId) {
        log.info(String.valueOf(LogMessages.TRY_GET_BOOKING), bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getByUserId(@RequestHeader(USERID) Integer userId,
                                        @RequestParam(defaultValue = "ALL") String state,
                                        @RequestParam(value = "from", defaultValue = "0") Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info(String.valueOf(LogMessages.TRY_GET_BOOKING_BY_USER_ID), userId);
        return bookingService.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwnerId(@RequestHeader(USERID) Integer userId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info(String.valueOf(LogMessages.TRY_GET_BOOKING_BY_OWNER_ID), userId);
        return bookingService.getByOwnerId(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USERID) Integer userId,
                                     @PathVariable Integer bookingId,
                                     @RequestParam boolean approved) {
        log.info(String.valueOf(LogMessages.TRY_APPROVE), bookingId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @PostMapping
    public BookingDto save(@RequestHeader(USERID) Integer userId,
                           @RequestBody BookingDto bookingDto) {
        log.info(String.valueOf(LogMessages.TRY_ADD), bookingDto);
        return bookingService.addNewBooking(userId, bookingDto);
    }
}