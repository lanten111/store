package com.example.store.exception;

import com.example.store.exception.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleExistException(AlreadyExistException e){
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBadNotFoundException(NotFoundException e){
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenException(ForbiddenException e){
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException  e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String developerMessage = e.getMessage();
        String userMessage =  errorMessages.get(0);
        logger.info(developerMessage);
        return new ResponseEntity<>(buildErrorResponse(userMessage, httpStatus.value()), httpStatus);
    }

    public  Map<String, String>  buildErrorResponse(String message, int errorCode){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("errorCode", String.valueOf(errorCode));
        return errorResponse;
    }
}
