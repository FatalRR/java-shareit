package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private void checkOwner(Integer userId, Item item) {
        if (!Objects.equals(userId, item.getUser().getId())) {
            throw new NotFoundException(ExceptionMessages.NOT_OWNER);
        }
    }

    private void checkUser(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }

    private List<ItemWithBooking> mapToItemWithBooking(Iterable<Item> items) {
        List<ItemWithBooking> itemWithBookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .collect(Collectors.groupingBy(Comment::getItem));

        Map<Item, List<Booking>> bookingsMap = bookingRepository.findByItemIn(items)
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem));

        for (Item item : items) {
            BookingDto lastBooking = getLastBookingDtoForItem(item, now, bookingsMap.getOrDefault(item, Collections.emptyList()));
            BookingDto nextBooking = getNextBookingDtoForItem(item, now, bookingsMap.getOrDefault(item, Collections.emptyList()));
            List<CommentDto> itemComments = CommentMapper.toDtoList(comments.getOrDefault(item, Collections.emptyList()));
            itemWithBookings.add(ItemMapper.toEntityWithBooking(item, lastBooking, nextBooking, itemComments));
        }

        return itemWithBookings;
    }

    private BookingDto getLastBookingDtoForItem(Item item, LocalDateTime now, List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isBefore(now))
                .max(Comparator.comparing(Booking::getStart))
                .map(BookingMapper::toDto)
                .orElse(null);
    }

    private BookingDto getNextBookingDtoForItem(Item item, LocalDateTime now, List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(Booking::getStart))
                .map(BookingMapper::toDto)
                .orElse(null);
    }

    @Override
    public Collection<ItemWithBooking> getItems(Integer userId) {
        checkUser(userId);
        return mapToItemWithBooking(itemRepository.findByUserId(userId));
    }

    @Transactional
    @Override
    public ItemDto save(Integer userId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException(ExceptionMessages.ITEM_BE_AVAILABLE);
        }
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(ExceptionMessages.USER_NOT_FOUND));
        Item item = ItemMapper.toEntity(user, itemDto);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public Item getById(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_ITEM));
    }

    @Transactional
    @Override
    public ItemDto update(Integer userId, ItemDto itemDto, Integer itemId) {
        checkUser(userId);
        Item item = itemRepository.findById(itemId).get();
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getByQuery(String query) {
        if (query.isBlank()) {
            return new ArrayList<>();
        }

        List<Item> userItems = itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        List<Item> availableItems = userItems.stream()
                .filter(Item::getAvailable)
                .collect(toList());

        return ItemMapper.mapToItemDto(availableItems);
    }

    @Transactional
    @Override
    public void removeById(Integer userId, Integer itemId) {
        checkUser(userId);
        checkOwner(userId, itemRepository.getById(itemId));
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public CommentDto addNewComment(Integer userId, CommentDto commentDto, Integer itemId) {
        List<Booking> bookings = bookingRepository.findBookingByUserIdAndFinishAfterNow(userId);
        boolean userIsBooker = bookings.stream()
                .anyMatch(booking -> Objects.equals(booking.getItem().getId(), itemId));

        if (!userIsBooker) {
            throw new ValidationException(ExceptionMessages.BOOKING_NOT_CONFIRMED);
        }

        Comment comment = CommentMapper.toEntity(commentDto);

        comment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_ITEM)));
        comment.setAuthorName(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.USER_NOT_FOUND))
                .getName());
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public ItemWithBooking getItemById(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_ITEM));

        BookingDto lastBooking = null;
        BookingDto nextBooking = null;
        if (Objects.equals(userId, item.getUser().getId())) {
            LocalDateTime currentDateTime = LocalDateTime.now();

            Optional<Booking> lastBookingOptional = bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), currentDateTime, Status.APPROVED);
            Optional<Booking> nextBookingOptional = bookingRepository
                    .findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(item.getId(), currentDateTime, Status.APPROVED);

            if (lastBookingOptional.isPresent()) {
                lastBooking = BookingMapper.toDto(lastBookingOptional.get());
            }
            if (nextBookingOptional.isPresent()) {
                Booking nextBookingForDto = nextBookingOptional.get();
                if (lastBookingOptional.isEmpty() || !lastBookingOptional.get().equals(nextBookingForDto)) {
                    nextBooking = BookingMapper.toDto(nextBookingForDto);
                }
            }
        }

        List<CommentDto> comments = CommentMapper.toDtoList(commentRepository.findAllByItemId(itemId));
        return ItemMapper.toEntityWithBooking(item, lastBooking, nextBooking, comments);
    }
}