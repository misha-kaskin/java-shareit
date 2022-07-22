package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll();
    }

    public UserDto getUserById(Long id) {
        Optional<UserDto> user = userRepository.getUserDtoById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException();
        }
    }

    public UserDto updateUserById(UserDto user, Long id) {
        if (user.getName() != null && user.getName().isEmpty()) {
            throw new ValidationException();
        }
        if (user.getId() != null) {
            throw new ValidationException();
        }
        UserDto lastUser = getUserById(id);
        if (user.getName() != null) {
            lastUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            lastUser.setEmail(user.getEmail());
        }
        try {
            return userRepository.save(lastUser);
        } catch (Exception e) {
            throw new DuplicateEmailException();
        }
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
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
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DuplicateEmailException();
        }

    }
}
