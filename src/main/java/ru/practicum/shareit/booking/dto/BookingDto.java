package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * // TODO .
 */
@Setter
@Getter
public class BookingDto {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private Long item;
    private Long booker;
    private String status;
}
