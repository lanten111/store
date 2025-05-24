package com.example.store.exception.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AlreadyExistException extends RuntimeException {

    private final String developerMessage;
    private final String message;

    public AlreadyExistException(String developerMessage, String message) {
        super(developerMessage);
        this.developerMessage = developerMessage;
        this.message = message;
    }
}
