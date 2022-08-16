package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void itemTest() {
        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.ru");
        user.setId(null);

        userService.createUser(user);

        Assertions.assertEquals(1L, itemService.createItem(item, 1L).getId());
        Assertions.assertEquals(1L, itemService.getItemById(1L, 1L).getId());
        Assertions.assertEquals(1, itemService.getItems(1L, null, null).size());
    }
}
