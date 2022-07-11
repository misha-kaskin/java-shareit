package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Repository
public interface ItemStorage {
    ItemDto createItem(ItemDto item, Long userId);

    ItemDto getItemById(Long id);

    ItemDto updateItemById(ItemDto item, Long id, Long userId);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> searchItems(String text);
}
