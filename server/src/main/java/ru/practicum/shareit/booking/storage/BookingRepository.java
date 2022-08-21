package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingDtoById(Long id);

    Page<Booking> findBookingDtoByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.itemId in (select i.id from Item i where i.owner = ?1)" +
            "order by b.start desc")
    Page<Booking> findBookingDtoByOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findBookingDtoByBookerIdAndItemIdOrderByEndAsc(Long bookerId, Long itemId);

    @Query("select b from Booking  b " +
            "where b.itemId = ?1 and b.start > ?2 " +
            "order by b.start asc")
    List<Booking> getNextBooking(Long itemId, LocalDateTime time);

    @Query("select b from Booking  b " +
            "where b.itemId = ?1 and b.end < ?2 " +
            "order by b.end desc")
    List<Booking> getLastBooking(Long itemId, LocalDateTime time);
}
