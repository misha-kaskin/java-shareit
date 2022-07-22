package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDto, Long> {
    @Query("select u from UserDto u " +
            "where u.id = ?1")
    Optional<UserDto> getUserDtoById(Long id);

    @Query("select u from UserDto u " +
    "where u.email = ?1")
    Optional<UserDto> getUserDtoByEmail(String email);
}
