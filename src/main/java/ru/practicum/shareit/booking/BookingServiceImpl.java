package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto getBookingById(Integer userId, Integer bookingId) {
        checkUser(userId);
        Booking booking = checkBooking(bookingId);
        User user = booking.getBooker();
        User owner = booking.getItem().getUser();
        if (Objects.equals(userId, user.getId()) || Objects.equals(userId, owner.getId())) {
            return BookingMapper.toDto(booking);
        }
        throw new NotFoundException(String.format(ExceptionMessages.NOT_BOOKER, userId));
    }

    @Override
    public List<BookingDto> getByUserId(Integer userId, String state, Integer from, Integer size) {
        checkUser(userId);

        Sort sortByDate = Sort.by(Sort.Direction.ASC, "start");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        Status status = Status.valueOf(state.toUpperCase());
        switch (status) {
            case ALL:
                return BookingMapper.toDtoList(bookingRepository.getByBookerIdOrderByStartDesc(userId, page).toList());
            case CURRENT:
                return BookingMapper.toDtoList(bookingRepository.getCurrentByUserId(userId, page).toList());
            case PAST:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndFinishAfterNow(userId, page).toList());
            case FUTURE:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndStarBeforeNow(userId, page).toList());
            case WAITING:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, Status.WAITING, page).toList());
            case REJECTED:
                return BookingMapper.toDtoList(bookingRepository.findBookingByUserIdAndByStatusContainingIgnoreCase(userId, Status.REJECTED, page).toList());
        }
        throw new ValidationException(String.format(ExceptionMessages.UNKNOWN_STATUS));
    }

    @Override
    public List<BookingDto> getByOwnerId(Integer userId, String state, Integer from, Integer size) {
        checkUser(userId);

        Sort sortByDate = Sort.by(Sort.Direction.ASC, "start");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        Status status = Status.valueOf(state.toUpperCase());
        switch (status) {
            case ALL:
                return BookingMapper.toDtoList(bookingRepository.findByOwnerId(userId, page).toList());
            case CURRENT:
                return BookingMapper.toDtoList(bookingRepository.getCurrentByOwnerId(userId, page).toList());
            case PAST:
                return BookingMapper.toDtoList(bookingRepository.findPastByOwnerId(userId, page).toList());
            case FUTURE:
                return BookingMapper.toDtoList(bookingRepository.findBookingByOwnerIdAndStarBeforeNow(userId, page).toList());
            case WAITING:
                return BookingMapper.toDtoList(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, Status.WAITING, page).toList());
            case REJECTED:
                return BookingMapper.toDtoList(bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, Status.REJECTED, page).toList());
        }
        throw new ValidationException(String.format(ExceptionMessages.UNKNOWN_STATUS));
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Integer userId, Integer bookingId, boolean approve) {
        checkUser(userId);
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
        return BookingMapper.toDto(booking);
    }

    @Transactional
    @Override
    public BookingDto addNewBooking(Integer userId, BookingDto bookingDto) {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidationException(ExceptionMessages.DATE_BOOKING);
        }
        User user = checkUser(userId);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException(String.format(ExceptionMessages.NOT_ITEM, bookingDto.getItemId())));
        if (Objects.equals(userId, item.getUser().getId())) {
            throw new NotFoundException(String.format(ExceptionMessages.NOT_BOOKER, userId));
        }
        if (!item.getAvailable()) {
            throw new ValidationException(ExceptionMessages.ITEM_BE_AVAILABLE);
        }
        Booking booking = BookingMapper.toEntity(user, item, bookingDto);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toDto(booking);
    }

    private Booking checkBooking(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format(ExceptionMessages.NOT_FOUND_BOOKING, bookingId)));
    }

    private User checkUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }
}