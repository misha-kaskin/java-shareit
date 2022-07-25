package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.model.CommentDto;
import ru.practicum.shareit.comment.dto.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    ItemDto createItem(ItemDto item, Long userId);

    ItemDto getItemById(Long id, Long userId);

    ItemDto updateItemById(ItemDto item, Long id, Long userId);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, Comment commentDto);
}
