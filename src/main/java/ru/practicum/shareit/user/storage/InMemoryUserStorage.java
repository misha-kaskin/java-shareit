package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, UserDto> users;
    private Long id;

    public InMemoryUserStorage() {
        users = new HashMap<>();
        id = 1L;
    }

    public boolean containsEmail(String email) {
        for (UserDto user : users.values()) {
            if (email.equals(user.getEmail())) {
                return true;
            }
        }

        return false;
    }

    public boolean isContainsUser(Long id) {
        return users.containsKey(id);
    }

    public UserDto create(UserDto user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    public List<UserDto> listAll() {
        return new ArrayList<>(users.values());
    }

    public UserDto getById(Long id) {
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

    public UserDto update(UserDto user, Long id) {
        if (users.containsKey(id)) {
            UserDto changedUser = users.get(id);
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
