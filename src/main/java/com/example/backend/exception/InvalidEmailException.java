package com.example.backend.exception;

public class InvalidEmailException extends NullPointerException{
    public InvalidEmailException(String email) {
        super("Пользователя с почтой " + email + " не существует");
    }
}
