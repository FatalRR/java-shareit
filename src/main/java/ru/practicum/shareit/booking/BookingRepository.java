package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findById(Integer id);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Integer id, LocalDateTime start, Status status);

    Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(Integer id, LocalDateTime end, Status status);

    Page<Booking> getByBookerIdOrderByStartDesc(Integer userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.status like ?2 " +
            "order by b.start desc")
    Page<Booking> findBookingByUserIdAndByStatusContainingIgnoreCase(Integer userId, Status state, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp  " +
            "order by b.start")
    Page<Booking> getCurrentByUserId(Integer userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    Page<Booking> findBookingByUserIdAndFinishAfterNow(Integer userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    Page<Booking> findBookingByUserIdAndStarBeforeNow(Integer userId, Pageable page);


    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    Page<Booking> findByOwnerId(Integer userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.status like ?2 " +
            "order by b.start desc")
    Page<Booking> findBookingByOwnerIdAndByStatusContainingIgnoreCase(Integer userId, Status state, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp " +
            "order by b.start desc")
    Page<Booking> getCurrentByOwnerId(Integer userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    Page<Booking> findPastByOwnerId(Integer userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    Page<Booking> findBookingByOwnerIdAndStarBeforeNow(Integer userId, Pageable page);

    List<Booking> findByItemIn(Iterable<Item> items);

    List<Booking> getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(Integer userId, Integer itemId, LocalDateTime end);

    Boolean existsByBookerIdAndItemIdAndEndBefore(Integer bookerId,
                                                  Integer itemId,
                                                  LocalDateTime dateTime);
}