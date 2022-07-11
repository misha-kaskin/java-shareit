package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers();
    }

    public UserDto getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public UserDto updateUserById(UserDto user, Long id) {
        if (user.getEmail() != null) {
            if (userStorage.containsEmail(user.getEmail())) {
                throw new DuplicateEmailException();
            }
        }
        if (user.getName() != null) {
            if (user.getName().isEmpty()) {
                throw new ValidationException();
            }
        }
        if (user.getId() != null) {
            throw new ValidationException();
        }
        return userStorage.updateUserById(user, id);
    }

    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }

    public UserDto createUser(UserDto user) {
        if (user.getEmail() == null || user.getName() == null) {
            throw new ValidationException();
        }
        if (user.getId() != null) {
            throw new ValidationException();
        }
        if (user.getName().isEmpty()) {
            throw new ValidationException();
        }
        if (userStorage.containsEmail(user.getEmail())) {
            throw new DuplicateEmailException();
        }
        return userStorage.createUser(user);
    }
}
