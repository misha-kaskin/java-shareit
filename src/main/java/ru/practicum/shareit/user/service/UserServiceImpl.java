package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> getUsers() {
        return userStorage.listAll();
    }

    public UserDto getUserById(long id) {
        return userStorage.getById(id);
    }

    public UserDto updateUserById(UserDto user, Long id) {
        if (user.getEmail() != null && userStorage.containsEmail(user.getEmail())) {
            throw new DuplicateEmailException();
        }
        if (user.getName() != null && user.getName().isEmpty()) {
            throw new ValidationException();
        }
        if (user.getId() != null) {
            throw new ValidationException();
        }
        return userStorage.update(user, id);
    }

    public void deleteUserById(Long id) {
        userStorage.delete(id);
    }

    public UserDto createUser(UserDto user) {
        if (!StringUtils.hasText(user.getEmail())) {
            throw new ValidationException();
        }
        if (user.getId() != null) {
            throw new ValidationException();
        }
        if (!StringUtils.hasText(user.getName())) {
            throw new ValidationException();
        }
        if (userStorage.containsEmail(user.getEmail())) {
            throw new DuplicateEmailException();
        }
        return userStorage.create(user);
    }
}
