package com.example.store.exception;

import com.example.store.exception.exceptions.*;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private final MessageSource messageSource;

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleExistException(AlreadyExistException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBadNotFoundException(NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadNotFoundException(BadCredentialsException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        logger.info(e.getMessage());
        String message = messageSource.getMessage("store.auth.invalid.details", null, Locale.getDefault());
        return new ResponseEntity<>(buildErrorResponse(message, httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbiddenException(ForbiddenException e) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        logger.info(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnhandledGeneralException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String developerMessage = e.getMessage();
        String message = messageSource.getMessage("store.general.error", null, Locale.getDefault());
        logger.error(developerMessage, e);
        return new ResponseEntity<>(buildErrorResponse(message, httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleUnhandledGeneralException(NoResourceFoundException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String developerMessage = e.getMessage();
        String message = messageSource.getMessage("store.general.error", null, Locale.getDefault());
        logger.error(developerMessage, e);
        return new ResponseEntity<>(buildErrorResponse(message, httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String developerMessage = e.getMessage();
        String message = errorMessages.get(0);
        logger.info(developerMessage);
        return new ResponseEntity<>(buildErrorResponse(message, httpStatus.value()), httpStatus);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        logger.error(e.getDeveloperMessage());
        return new ResponseEntity<>(buildErrorResponse(e.getMessage(), httpStatus.value()), httpStatus);
    }

    public Map<String, String> buildErrorResponse(String message, int errorCode) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        errorResponse.put("errorCode", String.valueOf(errorCode));
        return errorResponse;
    }
}
