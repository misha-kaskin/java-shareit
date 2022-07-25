package ru.practicum.shareit.requests.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Setter
@Getter
public class ItemRequest {
    private long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
}
