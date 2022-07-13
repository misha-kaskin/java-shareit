package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setOwner(item.getOwner().getId());
        itemDto.setRequest(item.getRequest().getId());
        itemDto.setId(item.getId());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());

        return itemDto;
    }
}
