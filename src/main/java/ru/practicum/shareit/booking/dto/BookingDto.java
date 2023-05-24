package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.messages.ExceptionMessages;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Integer id;

    @NotNull(message = ExceptionMessages.NOT_EMPTY_ID)
    private Integer itemId;

    private Integer bookerId;

    @NotNull(message = ExceptionMessages.NOT_EMPTY_DATE)
    @Future(message = ExceptionMessages.NOT_FUTURE_DATE)
    private LocalDateTime start;

    @NotNull(message = ExceptionMessages.NOT_EMPTY_DATE)
    @Future(message = ExceptionMessages.NOT_FUTURE_DATE)
    private LocalDateTime end;
}