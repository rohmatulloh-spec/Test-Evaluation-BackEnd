package com.example.demo.services.cache;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImplement implements ICacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String generateCustomerCacheKey(Integer pageNo, Integer pageSize) {
        return String.format("customers:pageNo:%d:pageSize:%d", pageNo, pageSize);
    }

    @Override
    public void deleteCustomerCacheKeys() {
        String pattern = "customers:pageNo:*:pageSize:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

}

