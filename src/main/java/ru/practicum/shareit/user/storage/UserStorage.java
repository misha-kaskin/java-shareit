package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    boolean containsEmail(String email);

    boolean isContainsUser(Long id);

    UserDto create(UserDto user);

    List<UserDto> listAll();

    UserDto getById(Long id);

    void delete(Long id);

    UserDto update(UserDto user, Long id);
}
