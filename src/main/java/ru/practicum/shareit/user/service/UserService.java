package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUserById(Long id);

    User updateUserById(User user, Long id);

    void deleteUserById(Long id);

    User createUser(User user);
}
