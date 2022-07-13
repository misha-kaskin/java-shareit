package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * // TODO .
 */
@Setter
@Getter
public class User {
    private Long id;
    private String name;
    @Email
    private String email;
}
