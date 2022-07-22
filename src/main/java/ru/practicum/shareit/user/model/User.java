package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;

/**
 * // TODO .
 */
@Setter
@Getter
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    @Email
    private String email;
}
