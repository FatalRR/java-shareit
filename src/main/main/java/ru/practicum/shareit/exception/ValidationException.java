package ru.practicum.shareit.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}