package ru.practicum.shareit.requests.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long requester;
    private LocalDateTime created;
}
