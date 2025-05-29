package com.example.store.security;

import com.example.store.config.MessagesSource;
import com.example.store.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws ServletException, IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    String message =  messagesSource.messageSource().getMessage("store.auth.error", null, request.getLocale());
    String developerMessage = authException.getMessage();
    int errorCode = HttpServletResponse.SC_UNAUTHORIZED;

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(),exceptionHandler.buildErrorResponse(message, errorCode));
  }
}