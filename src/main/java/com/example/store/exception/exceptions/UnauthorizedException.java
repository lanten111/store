package com.example.store.exception.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnauthorizedException extends RuntimeException {

    private final String developerMessage;
    private final String message;

    public UnauthorizedException(String developerMessage, String message) {
        super(developerMessage);
        this.developerMessage = developerMessage;
        this.message = message;
    }
}
