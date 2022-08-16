package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item toItemDto(ItemDto item) {
        Item itemDto = new Item();

        itemDto.setOwner(item.getOwner().getId());
        itemDto.setRequestId(item.getRequest().getId());
        itemDto.setId(item.getId());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());

        return itemDto;
    }
}
