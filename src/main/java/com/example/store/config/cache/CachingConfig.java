package com.example.store.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@EnableCaching
public class CachingConfig {

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public KeyGenerator customKeyGenerator() {
        return new CustomKeyGenerator();
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(n -> {
                    cacheManager.getCache(n).clear();
                });
    }
}
