package com.example.backend.exception;

public class IncorrectPasswordException extends RuntimeException{
    public IncorrectPasswordException(String passwordNotCorrect) {
        super(passwordNotCorrect);
    }
}
