package com.example.store.security.filters;

import com.example.store.config.MessagesSource;
import com.example.store.exception.exceptions.UnauthorizedException;
import com.example.store.security.TokenServiceImpl;
import com.example.store.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

  private final TokenServiceImpl tokenService;
  private final UserDetailsServiceImpl userDetailsService;
  private final MessagesSource messagesSource;
  private final RedisTemplate<String, Object> redisTemplate;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try {
        String jwt = parseJwt(request);
        if (jwt != null && redisTemplate.opsForValue().get(tokenService.getEmailFromToken(jwt)) != null && tokenService.validateToken(jwt) ) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(tokenService.getEmailFromToken(jwt));
          UsernamePasswordAuthenticationToken authentication =
                  new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
        String developerMessage = e.getMessage();
        String userMessage =  messagesSource.messageSource().getMessage("store.auth.error", null, request.getLocale());
        throw new UnauthorizedException(developerMessage, userMessage);
      }
      filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }
}