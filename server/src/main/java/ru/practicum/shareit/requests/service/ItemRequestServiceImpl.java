package ru.practicum.shareit.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository,
                                  ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public ItemRequest addItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        itemRequestDto.setRequester(userId);
        itemRequestDto.setCreated(LocalDateTime.now());

        return itemRequestRepository.save(convert(itemRequestDto));
    }

    public List<ItemRequest> getItemRequest(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        return itemRequestRepository.getItemRequestByRequesterIdOrderByCreatedAsc(userId)
                .stream()
                .peek(this::addItems)
                .collect(Collectors.toList());
    }

    public List<ItemRequest> getAllItemRequest(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        List<ItemRequest> itemRequests = itemRequestRepository
                    .findAllItemRequest(userId, PageRequest.of(from, size))
                    .toList();

        return itemRequests.stream()
                .peek(this::addItems)
                .collect(Collectors.toList());
    }

    public ItemRequest getItemRequestById(Long userId, Long id) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!itemRequestRepository.existsById(id)) {
            throw new NotFoundException();
        }

        ItemRequest itemRequest = itemRequestRepository.findItemRequestById(id);
        addItems(itemRequest);

        return itemRequest;
    }

    private ItemRequest convert(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setRequesterId(itemRequestDto.getRequester());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated());

        return itemRequest;
    }

    private void addItems(ItemRequest itemRequest) {
        itemRequest.setItems(itemRepository.findItemByRequestId(itemRequest.getId()));
    }
}
