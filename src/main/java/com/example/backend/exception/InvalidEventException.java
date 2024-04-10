package com.example.backend.exception;

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(String id) {
        super("Игра с таким идентификатором не существует: " + id);
    }
}
