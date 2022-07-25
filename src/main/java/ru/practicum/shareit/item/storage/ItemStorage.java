package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    ItemDto create(ItemDto item, Long userId);

    ItemDto getById(Long id);

    ItemDto update(ItemDto item, Long id, Long userId);

    List<ItemDto> listAll(Long userId);

    List<ItemDto> search(String text);
}
