package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item createItem(@RequestBody Item item,
                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createItem(item, userId);
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id,
                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(id, userId);
    }

    @PatchMapping("/{id}")
    public Item updateItemById(@RequestBody Item item,
                               @PathVariable Long id,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItemById(item, id, userId);
    }

    @GetMapping
    public List<Item> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemService.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody Comment commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
