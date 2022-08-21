package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

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

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findUserDtoById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException();
        }
    }

    public User updateUserById(User user, Long id) {
        User lastUser = getUserById(id);

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

    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DuplicateEmailException();
        }
    }
}
