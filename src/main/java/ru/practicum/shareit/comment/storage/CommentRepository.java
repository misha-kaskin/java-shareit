package ru.practicum.shareit.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentDto, Long> {
    @Query("select c from CommentDto c " +
            "where c.itemId = ?1")
    List<CommentDto> getCommentDtoByItemId(Long itemId);
}
