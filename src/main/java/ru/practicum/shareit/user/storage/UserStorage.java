package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Repository
public interface UserStorage {
    boolean containsEmail(String email);

    boolean isContainsUser(Long id);

    UserDto createUser(UserDto user);

    List<UserDto> getUsers();

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    UserDto updateUserById(UserDto user, Long id);
}
