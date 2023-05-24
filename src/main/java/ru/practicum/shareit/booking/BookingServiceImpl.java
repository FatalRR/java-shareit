package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Override
    public Booking getBookingById(Integer userId, Integer bookingId) {
        userService.getById(userId);
        Booking booking = checkBooking(bookingId);
        User user = booking.getBooker();
        User owner = booking.getItem().getUser();
        if (Objects.equals(userId, user.getId()) || Objects.equals(userId, owner.getId())) {
            return booking;
        }
        throw new NotFoundException(String.format(ExceptionMessages.NOT_BOOKER, userId));
    }

    @Override
    public List<Booking> getByUserId(Integer userId, String state) {
        userService.getById(userId);
        Status status = Status.valueOf(state.toUpperCase());
        switch (status) {
            case ALL:
                return bookingRepository.findByUserId(userId);
            case CURRENT:
                return bookingRepository.findCurrentByUserId(userId);
            case PAST:
                return bookingRepository.findBookingByUserIdAndFinishAfterNow(userId);
            case FUTURE:
                return bookingRepository.findBookingByUserIdAndStarBeforeNow(userId);
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, status);
        }
        throw new ValidationException(String.format(ExceptionMessages.UNKNOWN_STATUS));
    }

    @Override
    public List<Booking> getByOwnerId(Integer userId, String state) {
        userService.getById(userId);
        Status status = Status.valueOf(state.toUpperCase());
        switch (status) {
            case ALL:
                return bookingRepository.findByOwnerId(userId);
            case CURRENT:
                return bookingRepository.findCurrentByOwnerId(userId);
            case PAST:
                return bookingRepository.findPastByOwnerId(userId);
            case FUTURE:
                return bookingRepository.findBookingByOwnerIdAndStarBeforeNow(userId);
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, status);
        }
        throw new ValidationException(String.format(ExceptionMessages.UNKNOWN_STATUS));
    }

    @Transactional
    @Override
    public Booking approveBooking(Integer userId, Integer bookingId, boolean approve) {
        userService.getById(userId);
        Booking booking = checkBooking(bookingId);
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException(String.format(ExceptionMessages.ALREADY_APPROVED, bookingId));
        }
        User owner = booking.getItem().getUser();
        if (!Objects.equals(userId, owner.getId())) {
            throw new NotFoundException(String.format(ExceptionMessages.NOT_FOUND_BOOKING, bookingId));
        }
        if (approve) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return booking;
    }

    @Transactional
    @Override
    public Booking addNewBooking(Integer userId, BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidationException(ExceptionMessages.DATE_BOOKING);
        }
        User user = userService.getById(userId);
        Item item = itemService.getById(bookingDto.getItemId());
        if (Objects.equals(userId, item.getUser().getId())) {
            throw new NotFoundException(String.format(ExceptionMessages.NOT_BOOKER, userId));
        }
        if (!item.getAvailable()) {
            throw new ValidationException(ExceptionMessages.ITEM_BE_AVAILABLE);
        }
        Booking booking = BookingMapper.toEntity(user, item, bookingDto);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return booking;
    }

    private Booking checkBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format(ExceptionMessages.NOT_FOUND_BOOKING, bookingId)));
    }
}