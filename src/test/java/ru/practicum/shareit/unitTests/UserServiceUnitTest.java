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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceUnitTest {
    @MockBean
    private final UserRepository userRepository;
    private final UserService userService;

    @Test
    void userSaveTest() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        Assertions.assertEquals(userService.createUser(user).getEmail(), user.getEmail());
    }

    @Test
    void userSaveTestShouldThrowValidationException() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("");

        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        final ValidationException validationException = Assertions
                .assertThrows(ValidationException.class, () -> userService.createUser(user));

        Assertions.assertEquals("Пустая строка", validationException.getMessage());
    }

    @Test
    void userGetByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.ru");
        user.setName("user");

        Mockito
                .when(userRepository.findUserDtoById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(user.getEmail(), userService.getUserById(user.getId()).getEmail());
    }

    @Test
    void userGetByIdTestShouldThrowNotFoundException() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        Mockito
                .when(userRepository.findUserDtoById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void userGetTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.ru");
        user.setName("user");

        List<User> users = List.of(user);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user.getEmail(), users.get(0).getEmail());
    }

    @Test
    void userUpdateTest() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        Mockito
                .when(userRepository.findUserDtoById(1L))
                .thenReturn(Optional.of(user));

        Mockito
                .when(userRepository.save(user))
                .thenReturn(user);

        Assertions.assertEquals(userService.updateUserById(user, 1L), user);
    }

    @Test
    void userUpdateTestShouldThrowValidationException() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.ru");
        user.setName("user");

        Assertions.assertThrows(ValidationException.class, () -> userService.updateUserById(user, 1L));
    }

    @Test
    void userUpdateTestShouldThrowNotFoundException() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        Mockito
                .when(userRepository.findUserDtoById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.updateUserById(user, 1L));
    }
}
