package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);
    
    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getBookings(String state, Long userId);

    Booking approve(String approved, Long bookingId, Long userId);

    List<Booking> getOwnerBookings(String state, Long userId);
}
