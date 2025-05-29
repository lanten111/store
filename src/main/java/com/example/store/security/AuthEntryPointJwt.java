package com.example.store.security;

import com.example.store.config.MessagesSource;
import com.example.store.exception.GlobalExceptionHandler;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private final MessagesSource messagesSource;
    private final GlobalExceptionHandler exceptionHandler;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws ServletException, IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = messagesSource.messageSource().getMessage("store.auth.error", null, request.getLocale());
        String developerMessage = authException.getMessage();
        int errorCode = HttpServletResponse.SC_UNAUTHORIZED;

        logger.error("Attemt on unauthenticated resource", authException.getMessage());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), exceptionHandler.buildErrorResponse(message, errorCode));
    }
}
