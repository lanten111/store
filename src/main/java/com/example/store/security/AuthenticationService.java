package com.example.store.security;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.TokenDTO;

import com.example.store.exception.exceptions.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    private final TokenServiceImpl tokenService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public TokenDTO login(CustomerDTO customerDTO, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(customerDTO.getEmail(), customerDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("user with email {} loggedin", customerDTO.getEmail());
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(tokenService.generateToken(authentication));
        return tokenDTO;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
                UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
                redisTemplate.opsForValue().getAndDelete(userPrincipal.getUsername());
                logger.info("user with email {} logged out", (userPrincipal.getUsername()));
            }else {
                throw new GeneralException("security context is empty", "Cannot log out as no active session is found.");
            }
        }
}
