package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * // TODO .
 */
@Getter
@Setter
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    @JsonIgnore
    private Long owner;
    @JsonIgnore
    private Long request;
}
