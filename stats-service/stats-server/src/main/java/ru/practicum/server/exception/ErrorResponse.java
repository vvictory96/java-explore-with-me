package ru.practicum.server.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
}
