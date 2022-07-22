package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.model.Comment;

import javax.persistence.*;
import java.util.List;

/**
 * // TODO .
 */
@Getter
@Setter
@Entity
@Table(name = "items")
public class ItemDto {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "available")
    private Boolean available;
    @JsonIgnore
    @Column(name = "owner_id")
    private Long owner;
    @JsonIgnore
    @Column(name = "request_id")
    private Long request;
    @Transient
    private BookingDto lastBooking;
    @Transient
    private BookingDto nextBooking;
    @Transient
    private List<Comment> comments;
}
