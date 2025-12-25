package com.scaler.assignment.currency.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> restTemplate;

    public void set(String key, Object value) {
        restTemplate.opsForValue().set(key, value);
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object val = restTemplate.opsForValue().get(key);
        if (val == null) {
            return Optional.empty();
        }
        return Optional.of(clazz.cast(val));
    }
}
