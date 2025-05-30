package com.example.store.config.properties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheTtlProperties {
    private Map<String, Long> ttl = new HashMap<>();
}
