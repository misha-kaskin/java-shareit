package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public interface ItemService {
    ItemDto createItem(ItemDto item, Long userId);

    ItemDto getItemById(Long id, Long userId);

    ItemDto updateItemById(ItemDto item, Long id, Long userId);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> searchItems(String text);

    Comment addComment(Long userId, Long itemId, CommentDto commentDto);
}
