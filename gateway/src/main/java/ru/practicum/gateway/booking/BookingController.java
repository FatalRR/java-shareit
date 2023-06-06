package ru.practicum.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.booking.dto.BookingDto;
import ru.practicum.gateway.messages.LogMessages;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    public static final String USERID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(USERID) Integer userId,
                                       @RequestBody @Valid BookingDto bookingDto) {
        log.info(String.valueOf(LogMessages.TRY_ADD), bookingDto);
        return bookingClient.addNewBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USERID) Integer userId,
                                                 @PathVariable Integer bookingId,
                                                 @RequestParam boolean approved) {
        log.info(String.valueOf(LogMessages.TRY_APPROVE), bookingId);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(USERID) Integer userId,
                                          @PathVariable Integer bookingId) {
        log.info(String.valueOf(LogMessages.TRY_GET_BOOKING), bookingId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(USERID) Integer userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info(String.valueOf(LogMessages.TRY_GET_BOOKING_BY_USER_ID), userId);
        return bookingClient.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(USERID) Integer userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String state,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info(String.valueOf(LogMessages.TRY_GET_BOOKING_BY_OWNER_ID), userId);
        return bookingClient.getByOwnerId(userId, state, from, size);
    }
}