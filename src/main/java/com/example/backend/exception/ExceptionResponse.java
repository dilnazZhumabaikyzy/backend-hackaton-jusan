package com.example.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
}
