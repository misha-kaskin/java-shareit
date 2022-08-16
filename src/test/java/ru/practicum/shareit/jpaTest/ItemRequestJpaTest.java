package ru.practicum.shareit.jpaTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestJpaTest {
    private final TestEntityManager entityManager;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Test
    void itemRequestTest() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        userRepository.save(user);

        User otherUser = new User();
        otherUser.setId(null);
        otherUser.setEmail("otherUser@mail.ru");
        otherUser.setName("otherUser");

        userRepository.save(otherUser);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("description");
        itemRequest.setRequesterId(1L);

        Assertions.assertNull(itemRequest.getId());

        itemRequestRepository.save(itemRequest);

        Assertions.assertNotNull(itemRequest.getId());

        Assertions.assertEquals(1, itemRequestRepository.findAllItemRequest(2L).size());
        Assertions.assertEquals(0, itemRequestRepository.findAllItemRequest(1L).size());
    }
}
