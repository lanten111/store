package com.example.store.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private String errorCode;
}
