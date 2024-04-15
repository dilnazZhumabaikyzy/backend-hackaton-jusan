package com.example.backend.exception;

public class InvalidEventException extends NullPointerException {
    public InvalidEventException(String id) {
        super("Игра с таким идентификатором не существует: " + id);
    }
}
