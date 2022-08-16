package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item, Long userId);

    Item getItemById(Long id, Long userId);

    Item updateItemById(Item item, Long id, Long userId);

    List<Item> getItems(Long userId, Integer from, Integer size);

    List<Item> searchItems(String text, Integer from, Integer size);

    CommentDto addComment(Long userId, Long itemId, Comment commentDto);
}
