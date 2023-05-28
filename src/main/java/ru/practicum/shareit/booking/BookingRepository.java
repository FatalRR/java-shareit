package ru.practicum.shareit.booking;

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

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    List<Booking> findByUserId(Integer userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.status like ?2 " +
            "order by b.start desc")
    List<Booking> findBookingByUserIdAndByStatusContainingIgnoreCase(Integer userId, Status state);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp  " +
            "order by b.start desc")
    List<Booking> findCurrentByUserId(Integer userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findBookingByUserIdAndFinishAfterNow(Integer userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findBookingByUserIdAndStarBeforeNow(Integer userId);


    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    List<Booking> findByOwnerId(Integer userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.status like ?2 " +
            "order by b.start desc")
    List<Booking> findBookingByOwnerIdAndByStatusContainingIgnoreCase(Integer userId, Status state);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp " +
            "order by b.start desc")
    List<Booking> findCurrentByOwnerId(Integer userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findPastByOwnerId(Integer userId);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findBookingByOwnerIdAndStarBeforeNow(Integer userId);

    List<Booking> findByItemIn(Iterable<Item> items);
}