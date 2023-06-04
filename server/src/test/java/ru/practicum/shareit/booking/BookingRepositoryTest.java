package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    Integer from = 1;
    Integer size = 10;
    Integer pageIndex = from / size;
    Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
    Pageable page = PageRequest.of(pageIndex, size, sortByDate);

    @BeforeEach
    void addBookings() {
        Booking booking1 = new Booking();
        User user1 = new User();
        user1.setName("name1");
        user1.setEmail("test1@mail.ru");
        user1.setId(1);
        userRepository.save(user1);
        booking1.setBooker(user1);
        Item item1 = new Item();
        item1.setId(1);
        item1.setDescription("description1");
        item1.setAvailable(true);
        item1.setUser(user1);
        item1.setName("item1");
        itemRepository.save(item1);
        booking1.setItem(item1);
        booking1.setStatus(Status.WAITING);
        booking1.setStart(LocalDateTime.of(2021, 1, 1, 7, 1));
        booking1.setEnd(LocalDateTime.of(2022, 1, 1, 1, 1));
        bookingRepository.save(booking1);
        Booking booking2 = new Booking();
        User user2 = new User();
        user2.setName("name2");
        user2.setEmail("test2@mail.ru");
        user2.setId(2);
        userRepository.save(user2);
        booking2.setBooker(user2);
        Item item2 = new Item();
        item2.setId(2);
        item2.setDescription("description2");
        item2.setAvailable(true);
        item2.setUser(user2);
        item2.setName("item2");
        itemRepository.save(item2);
        booking2.setItem(item2);
        booking2.setStatus(Status.WAITING);
        booking2.setStart(LocalDateTime.of(2023, 2, 2, 2, 2));
        booking2.setEnd(LocalDateTime.of(2024, 2, 2, 2, 2));
        bookingRepository.save(booking2);
        Booking booking3 = new Booking();
        User user3 = new User();
        user3.setName("name3");
        user3.setEmail("test3@mail.ru");
        user3.setId(3);
        userRepository.save(user3);
        booking3.setBooker(user3);
        Item item3 = new Item();
        item3.setId(3);
        item3.setDescription("description3");
        item3.setAvailable(true);
        item3.setUser(user3);
        item3.setName("item3");
        itemRepository.save(item3);
        booking3.setItem(item3);
        booking3.setStatus(Status.WAITING);
        booking3.setStart(LocalDateTime.of(2024, 1, 7, 7, 7));
        booking3.setEnd(LocalDateTime.of(2025, 1, 7, 7, 7));
        bookingRepository.save(booking3);
    }

    @Test
    void findByIdTest() {
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByStartDescTest() {
    }

    @Test
    void findFirstByItemIdAndEndAfterAndStatusOrderByStartAscTest() {
    }

    @Test
    void getByBookerIdOrderByStartDescTest() {
    }

    @Test
    void getByBookerIdAndStatusContainingIgnoreCaseOrderByStartDescTest() {
    }

    @Test
    void getCurrentByUserIdTest() {
        List<Booking> bookings = bookingRepository.getCurrentByUserId(2, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.get(0).getEnd().isAfter(LocalDateTime.now()));
    }

    @Test
    void getBookingByUserIdAndFinishAfterNowTest() {
        List<Booking> bookings = bookingRepository.findBookingByUserIdAndFinishAfterNow(1, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void getBookingByUserIdAndStarBeforeNowTest() {
        List<Booking> bookings = bookingRepository.findBookingByUserIdAndStarBeforeNow(3, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isAfter(LocalDateTime.now()));
    }

    @Test
    void findByOwnerIdTest() {
        List<Booking> bookings = bookingRepository.findByOwnerId(3, page).toList();

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0).getItem().getId(), 3);
    }

    @Test
    void getBookingByOwnerIdAndByStatusContainingIgnoreCaseTest() {
        List<Booking> bookings = bookingRepository.findBookingByOwnerIdAndByStatusContainingIgnoreCase(3, Status.valueOf("WAITING"), page).toList();

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0).getItem().getId(), 3);
    }

    @Test
    void getCurrentByOwnerIdTest() {
        List<Booking> bookings = bookingRepository.getCurrentByUserId(2, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.get(0).getEnd().isAfter(LocalDateTime.now()));
    }

    @Test
    void getPastByOwnerIdTest() {
        List<Booking> bookings = bookingRepository.findPastByOwnerId(1, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void getBookingByOwnerIdAndStarBeforeNowOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findBookingByOwnerIdAndStarBeforeNow(3, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isAfter(LocalDateTime.now()));
    }

    @Test
    void getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDescTest() {
    }
}