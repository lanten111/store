package com.example.store.security.property;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
    private String secret;
    private int expirationInSeconds;
}
