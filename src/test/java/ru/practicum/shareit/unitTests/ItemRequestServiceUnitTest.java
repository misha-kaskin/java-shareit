package ru.practicum.shareit.unitTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceUnitTest {
    @MockBean
    private final ItemRequestRepository itemRequestRepository;
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserRepository userRepository;
    private final ItemRequestService itemRequestService;

    @Test
    void addItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequesterId(1L);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequester(1L);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("description");
        itemRequestDto.setId(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        Assertions.assertEquals(1L, itemRequestService.addItemRequest(1L, itemRequestDto).getId());
    }

    @Test
    void addItemRequestTestShouldThrowNotFoundException() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequester(1L);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("description");
        itemRequestDto.setId(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.addItemRequest(1L, itemRequestDto));
    }

    @Test
    void addItemRequestTestShouldThrowValidationException() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequester(1L);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("");
        itemRequestDto.setId(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.addItemRequest(1L, itemRequestDto));
    }

    @Test
    void getItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequesterId(1L);

        List<ItemRequest> itemRequests = List.of(itemRequest);

        Item item = new Item();
        List<Item> items = List.of(item);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito
                .when(itemRequestRepository.getItemRequestByRequesterIdOrderByCreatedAsc(1L))
                .thenReturn(itemRequests);
        Mockito.when(itemRepository.findItemByRequestId(1L)).thenReturn(items);

        Assertions.assertEquals(1, itemRequestService.getItemRequest(1L).size());
        Assertions.assertEquals(1L, itemRequestService.getItemRequest(1L).get(0).getId());
    }

    @Test
    void getItemRequestTestShouldThrowNotFoundException() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequest(1L));
    }

    @Test
    void getAllItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequesterId(1L);

        List<ItemRequest> itemRequests = List.of(itemRequest);

        Item item = new Item();

        List<Item> items = List.of(item);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRequestRepository.findAllItemRequest(1L)).thenReturn(itemRequests);
        Mockito.when(itemRepository.findItemByRequestId(1L)).thenReturn(items);

        Assertions.assertEquals(1,
                itemRequestService.getAllItemRequest(1L, null, null).size());
        Assertions.assertEquals(1L,
                itemRequestService.getAllItemRequest(1L, null, null).get(0).getId());
    }

    @Test
    void getAllItemRequestTestShouldThrowNotFoundException() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getAllItemRequest(1L, null, null));
    }

    @Test
    void getAllItemRequestTestShouldThrowValidationException() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class,
                () -> itemRequestService.getAllItemRequest(1L, 1, null));
    }

    @Test
    void getItemRequestByIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequesterId(1L);

        Item item = new Item();

        List<Item> items = List.of(item);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRequestRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRequestRepository.findItemRequestById(1L)).thenReturn(itemRequest);
        Mockito.when(itemRepository.findItemByRequestId(1L)).thenReturn(items);

        Assertions.assertEquals(1L, itemRequestService.getItemRequestById(1L, 1L).getId());
    }

    @Test
    void getItemRequestByIdTestShouldThrowNotFoundException() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestById(1L, 1L));
    }
}
