package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * // TODO .
 */
@Setter
@Getter
public class UserDto {
    private Long id;
    private String name;
    @Email
    private String email;
}
