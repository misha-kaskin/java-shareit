package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item, Long userId);

    Item getById(Long id);

    Item update(Item item, Long id, Long userId);

    List<Item> listAll(Long userId);

    List<Item> search(String text);
}
