package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, ItemDto> items;
    private Long id;

    @Autowired
    public InMemoryItemStorage() {
        items = new HashMap<>();
        id = 1L;
    }

    public ItemDto createItem(ItemDto item, Long userId) {
        item.setId(id);
        item.setOwner(userId);
        items.put(id, item);
        id++;

        return item;
    }

    public ItemDto getItemById(Long id) {
        return items.get(id);
    }

    public ItemDto updateItemById(ItemDto item, Long id, Long userId) {
        if (items.containsKey(id)) {
            ItemDto changedItem = items.get(id);
            if (!userId.equals(changedItem.getOwner())) {
                throw new RuntimeException();
            }
            if (item.getAvailable() != null) {
                changedItem.setAvailable(item.getAvailable());
            }
            if (item.getName() != null) {
                changedItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                changedItem.setDescription(item.getDescription());
            }
            return changedItem;
        } else {
            throw new RuntimeException();
        }
    }

    public List<ItemDto> getItems(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }


    public List<ItemDto> searchItems(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
