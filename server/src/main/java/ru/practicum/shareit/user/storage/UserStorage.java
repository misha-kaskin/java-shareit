package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    boolean containsEmail(String email);

    boolean isContainsUser(Long id);

    User create(User user);

    List<User> listAll();

    User getById(Long id);

    void delete(Long id);

    User update(User user, Long id);
}
