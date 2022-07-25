package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking create(Booking bookingDto, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookings(String state, Long userId);

    BookingDto approve(String approved, Long bookingId, Long userId);

    List<BookingDto> getOwnerBookings(String state, Long userId);
}
