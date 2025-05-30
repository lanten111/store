package com.example.store.config.properties;

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
