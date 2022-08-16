package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestIntegrationTest {
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    void itemRequestServiceTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequesterId(1L);
        itemRequest.setDescription("description");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("description");
        itemRequestDto.setRequester(1L);

        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.ru");
        user.setId(null);

        userService.createUser(user);
        Assertions.assertEquals(1L,
                itemRequestService.addItemRequest(1L, itemRequestDto).getId());
        Assertions.assertEquals(1,
                itemRequestService.getItemRequest(1L).size());
        Assertions.assertEquals(0,
                itemRequestService.getAllItemRequest(1L, null, null).size());
    }
}
