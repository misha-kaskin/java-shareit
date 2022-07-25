package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items;
    private Long id;

    public InMemoryItemStorage() {
        items = new HashMap<>();
        id = 1L;
    }

    public Item create(Item item, Long userId) {
        item.setId(id);
        item.setOwner(userId);
        items.put(id, item);
        id++;

        return item;
    }

    public Item getById(Long id) {
        return items.get(id);
    }

    public Item update(Item item, Long id, Long userId) {
        if (items.containsKey(id)) {
            Item changedItem = items.get(id);
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
            throw new NotFoundException();
        }
    }

    public List<Item> listAll(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }


    public List<Item> search(String text) {
        return items.values()
                .stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }
}
