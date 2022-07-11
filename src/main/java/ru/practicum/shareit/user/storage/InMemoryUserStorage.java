package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, UserDto> users;
    private Long id;

    @Autowired
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

    public UserDto createUser(UserDto user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    public List<UserDto> getUsers() {
        return new ArrayList<>(users.values());
    }

    public UserDto getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException();
        }
    }

    public void deleteUserById(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException();
        }
    }

    public UserDto updateUserById(UserDto user, Long id) {
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
