package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingDto, Long> {
    @Query ("select b from BookingDto b " +
            "where b.id = ?1")
    Optional<BookingDto> findBookingDtoById(Long id);

    @Query ("select b from BookingDto b " +
            "where b.bookerId = ?1 " +
            "order by b.start desc")
    List<BookingDto> findBookingDtoByBookerId(Long bookerId);

    @Query ("select b from BookingDto b " +
            "where b.itemId in (select i.id from ItemDto i where i.owner = ?1)" +
            "order by b.start desc")
    List<BookingDto> findBookingDtoByOwnerId(Long ownerId);

    @Query("select b from BookingDto b " +
            "where b.bookerId = ?1 and b.itemId = ?2 " +
            "order by b.end asc ")
    List<BookingDto> findBookingDtoByBookerIdAndItemId(Long bookerId, Long itemId);

    @Query("select b from BookingDto  b " +
            "where b.itemId = ?1 and b.start > ?2 " +
            "order by b.start asc")
    List<BookingDto> getNextBooking(Long itemId, LocalDateTime time);

    @Query("select b from BookingDto  b " +
            "where b.itemId = ?1 and b.end < ?2 " +
            "order by b.end desc")
    List<BookingDto> getLastBooking(Long itemId, LocalDateTime time);
}
