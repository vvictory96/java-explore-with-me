package ru.practicum.server.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String msg) {
        super(msg);
    }

}
