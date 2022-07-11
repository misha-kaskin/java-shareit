package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * // TODO .
 */
@Setter
@Getter
public class Booking {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private String status;
}
