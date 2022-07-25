package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private Long id;

    public InMemoryUserStorage() {
        users = new HashMap<>();
        id = 1L;
    }

    public boolean containsEmail(String email) {
        for (User user : users.values()) {
            if (email.equals(user.getEmail())) {
                return true;
            }
        }

        return false;
    }

    public boolean isContainsUser(Long id) {
        return users.containsKey(id);
    }

    public User create(User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    public List<User> listAll() {
        return new ArrayList<>(users.values());
    }

    public User getById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException();
        }
    }

    public void delete(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException();
        }
    }

    public User update(User user, Long id) {
        if (users.containsKey(id)) {
            User changedUser = users.get(id);
            if (user.getEmail() != null) {
                changedUser.setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                changedUser.setName(user.getName());
            }
            return changedUser;
        } else {
            throw new NotFoundException();
        }
    }
}
