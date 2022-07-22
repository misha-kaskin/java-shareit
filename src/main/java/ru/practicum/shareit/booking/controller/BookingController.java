package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(bookingDto, userId);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getBookings(@RequestParam(defaultValue = "ALL") String state, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookings(state, userId);
    }

    @GetMapping("/owner")
    public List<Booking> getOwnerBookings(@RequestParam(defaultValue = "ALL") String state,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getOwnerBookings(state, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@RequestParam String approved,
                           @PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approve(approved, bookingId, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerNotFound(final NotFoundException e) {

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlerValidate(final ValidationException e) {

    }
}
