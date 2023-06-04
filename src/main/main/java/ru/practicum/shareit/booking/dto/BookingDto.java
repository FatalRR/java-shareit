package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Integer id;

    @NotNull(message = ExceptionMessages.NOT_EMPTY_ID)
    private Integer itemId;

    private Integer bookerId;

    @NotNull(message = ExceptionMessages.NOT_EMPTY_DATE)
    @Future(message = ExceptionMessages.NOT_FUTURE_DATE)
    private LocalDateTime start;

    @NotNull(message = ExceptionMessages.NOT_EMPTY_DATE)
    private LocalDateTime end;

    private Status status;

    private Booker booker;

    private ItemBooking item;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Booker {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemBooking {
        private Integer id;
        private String name;
    }
}