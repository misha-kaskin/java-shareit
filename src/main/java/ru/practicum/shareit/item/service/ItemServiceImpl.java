package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
            throw new NotFoundException();
        }
        if(!StringUtils.hasText(item.getName())) {
            throw new ValidationException();
        }
        if(!StringUtils.hasText(item.getDescription())) {
            throw new ValidationException();
        }
        if (item.getAvailable() == null) {
            throw new ValidationException();
        }
        return itemStorage.create(item, userId);
    }

    public ItemDto getItemById(Long id) {
        return itemStorage.getById(id);
    }

    public ItemDto updateItemById(ItemDto item, Long id, Long userId) {
        if (!userStorage.isContainsUser(userId)) {
            throw new NotFoundException();
        }
        if (!userId.equals(itemStorage.getById(id).getOwner())) {
            throw new NotFoundException();
        }
        if (item.getName() != null && item.getName().isEmpty()) {
            throw new ValidationException();
        }
        if (item.getDescription() != null && item.getDescription().isEmpty()) {
            throw new ValidationException();
        }
        return itemStorage.update(item, id, userId);
    }

    public List<ItemDto> getItems(Long userId) {
        if (!userStorage.isContainsUser(userId)) {
            throw new NotFoundException();
        }
        return itemStorage.listAll(userId);
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.search(text);
    }
}
