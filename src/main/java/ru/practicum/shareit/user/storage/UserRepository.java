package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    Optional<UserDto> findUserDtoById(Long id);
}
