package ru.practicum.shareit.requests.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Setter
@Getter
public class ItemRequestDto {
    private long id;
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
