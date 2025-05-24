package com.example.store.exception.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ForbiddenException extends RuntimeException {

    private final String developerMessage;
    private final String message;

    public ForbiddenException(String developerMessage, String message) {
        super(developerMessage);
        this.developerMessage = developerMessage;
        this.message = message;
    }
}
