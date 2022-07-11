package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }
    public ItemDto createItem(ItemDto item, Long userId) {
        if (!userStorage.isContainsUser(userId)) {
            throw new RuntimeException();
        }
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new RuntimeException();
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new RuntimeException();
        }
        if (item.getAvailable() == null) {
            throw new RuntimeException();
        }
        return itemStorage.createItem(item, userId);
    }

    public ItemDto getItemById(Long id) {
        return itemStorage.getItemById(id);
    }

    public ItemDto updateItemById(ItemDto item, Long id, Long userId) {
        if (!userStorage.isContainsUser(userId)) {
            throw new RuntimeException();
        }
        if (item.getName() != null && item.getName().isEmpty()) {
            throw new RuntimeException();
        }
        if (item.getDescription() != null && item.getDescription().isEmpty()) {
            throw new RuntimeException();
        }
        return itemStorage.updateItemById(item, id, userId);
    }

    public List<ItemDto> getItems(Long userId) {
        if (!userStorage.isContainsUser(userId)) {
            throw new RuntimeException();
        }
        return itemStorage.getItems(userId);
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItems(text);
    }
}
