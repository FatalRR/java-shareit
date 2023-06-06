package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Integer id;
    private Integer itemId;
    private Integer bookerId;
    private LocalDateTime start;
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