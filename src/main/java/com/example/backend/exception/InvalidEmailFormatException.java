package com.example.backend.exception;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(String email) {
        super("Неправильный формат почты: " + email);
    }
}
