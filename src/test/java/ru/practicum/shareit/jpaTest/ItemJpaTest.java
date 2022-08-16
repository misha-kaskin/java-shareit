package ru.practicum.shareit.jpaTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemJpaTest {
    private final TestEntityManager entityManager;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    void itemRepositoryTest() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        userRepository.save(user);

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setDescription("description");
        item.setId(null);
        item.setOwner(1L);

        Assertions.assertNull(item.getId());

        itemRepository.save(item);

        Assertions.assertNotNull(item.getId());

        Assertions.assertEquals(1, itemRepository.search("description").size());
        Assertions.assertEquals("name",
                itemRepository.search("description").get(0).getName());

        Assertions.assertEquals(0, itemRepository.search("Дрель").size());
    }
}
