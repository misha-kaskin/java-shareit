package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Long id);

    UserDto updateUserById(UserDto user, Long id);

    void deleteUserById(Long id);

    UserDto createUser(UserDto user);
}
