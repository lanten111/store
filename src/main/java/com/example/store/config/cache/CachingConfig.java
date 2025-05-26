package com.example.store.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CachingConfig {

    private final CacheTtlProperties cacheTtlProperties;

//    @Autowired
//    private CacheManager cacheManager;

    public CachingConfig(CacheTtlProperties cacheTtlProperties) {
        this.cacheTtlProperties = cacheTtlProperties;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        for (Map.Entry<String, Long> entry : cacheTtlProperties.getTtl().entrySet()) {
            cacheConfigurations.put(
                    entry.getKey(),
                    RedisCacheConfiguration.defaultCacheConfig()
                            .entryTtl(Duration.ofSeconds(entry.getValue()))
            );
        }

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public KeyGenerator customKeyGenerator() {
        return new CustomKeyGenerator();
    }

//    @EventListener
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        cacheManager.getCacheNames()
//                .parallelStream()
//                .forEach(n -> {
//                    cacheManager.getCache(n).clear();
//                });
//    }
}
