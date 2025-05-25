package com.example.store.config.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(target.getClass().getSimpleName());
        keyBuilder.append("#");
        keyBuilder.append(method.getName());
        for (Object param : params) {
            keyBuilder.append("#");
            keyBuilder.append(param.toString());
        }
        return keyBuilder.toString();
    }
}
