package ru.practicum.shareit.requests.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * // TODO .
 */
@Setter
@Getter
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @JsonIgnore
    private Long requesterId;
    private LocalDateTime created;
    @Transient
    private List<Item> items;
}
