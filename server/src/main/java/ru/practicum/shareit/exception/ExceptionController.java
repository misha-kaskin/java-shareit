package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerNotFound(final NotFoundException e) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlerValidate(final ValidationException e) {
        if (Objects.isNull(e.getMessage())) {
            return Map.of("error", "");
        } else {
            return Map.of("error", e.getMessage());
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handlerValidate(final DuplicateEmailException e) {

    }
}
