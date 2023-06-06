package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    void approveBookingTest() {
        UserDto userDto1 = new UserDto();
        userDto1.setEmail("1@1.ru");
        userDto1.setName("1");
        UserDto savedUserDto1 = userService.saveUser(userDto1);
        UserDto userDto2 = new UserDto();
        userDto2.setEmail("12@2.ru");
        userDto2.setName("2");
        UserDto savedUserDto2 = userService.saveUser(userDto2);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("i1");
        itemDto.setAvailable(true);
        itemService.save(savedUserDto1.getId(), itemDto);
        List<ItemWithBooking> itemsFromBase = (List<ItemWithBooking>) itemService.getItems(savedUserDto1.getId(), 1, 10);
        ItemWithBooking itemFromBase = itemsFromBase.get(0);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemFromBase.getId());
        bookingDto.setStart(LocalDateTime.of(2024, 7, 7, 7, 7));
        bookingDto.setEnd(LocalDateTime.of(2025, 7, 7, 7, 7));

        bookingService.addNewBooking(savedUserDto2.getId(), bookingDto);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.booker.id = :bookerId", Booking.class);
        Booking bookingFromBase = query.setParameter("bookerId", savedUserDto2.getId()).getSingleResult();

        assertThat(bookingFromBase.getStatus(), equalTo(Status.WAITING));

        bookingService.approveBooking(savedUserDto1.getId(), bookingFromBase.getId(), true);
        em.flush();

        Booking bookingFromBaseApproved = query.setParameter("bookerId", savedUserDto2.getId()).getSingleResult();

        assertThat(bookingFromBaseApproved.getStatus(), equalTo(Status.APPROVED));
    }
}