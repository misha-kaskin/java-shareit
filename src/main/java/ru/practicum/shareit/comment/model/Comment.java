package ru.practicum.shareit.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comment {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
