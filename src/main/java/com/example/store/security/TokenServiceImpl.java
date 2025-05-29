package com.example.store.security;

import com.example.store.config.MessagesSource;
import com.example.store.security.property.TokenProperties;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final TokenProperties tokenProperties;
    private final MessagesSource messagesSource;
    private final RedisTemplate<String, Object> redisTemplate;

    public String generateToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(convertToLocalDateToDate(
                        LocalDateTime.now().plusSeconds(tokenProperties.getExpirationInSeconds())))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
        redisTemplate
                .opsForValue()
                .set(userPrincipal.getUsername(), token, Duration.ofSeconds(tokenProperties.getExpirationInSeconds()));
        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenProperties.getSecret()));
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long getTokenRemainingTime(String token) {
        Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
        Claims claims = jwt.getBody();
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        long expirationTime = claims.getExpiration().getTime() / 1000;
        long remainingTimeSeconds = expirationTime - currentTimeMillis;

        return remainingTimeSeconds / 3600;
    }

    public boolean validateToken(String authToken) {
        boolean isValid = false;
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            isValid = true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.debug("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return isValid;
    }

    public static Date convertToLocalDateToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
