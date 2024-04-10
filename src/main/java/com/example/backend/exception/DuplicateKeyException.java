package com.example.backend.exception;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
