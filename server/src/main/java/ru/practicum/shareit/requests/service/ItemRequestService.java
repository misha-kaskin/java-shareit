package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequest> getItemRequest(Long userId);

    List<ItemRequest> getAllItemRequest(Long userId, Integer from, Integer size);

    ItemRequest getItemRequestById(Long userId, Long id);
}
