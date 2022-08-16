package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntegrationTest {
    private final UserService userService;

    @Test
    void userTest() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.ru");
        user.setId(null);

        User otherUser = userService.createUser(user);

        Assertions.assertEquals(1L, otherUser.getId());
        Assertions.assertEquals(1L, userService.getUserById(1L).getId());
        Assertions.assertEquals(1, userService.getUsers().size());

        userService.deleteUserById(1L);

        Assertions.assertEquals(0, userService.getUsers().size());
    }
}
