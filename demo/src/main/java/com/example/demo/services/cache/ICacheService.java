package com.example.demo.services.cache;

import org.springframework.stereotype.Service;

@Service
public interface ICacheService {
    
    public String generateCustomerCacheKey(Integer pageNo, Integer pageSize);

    public void deleteCustomerCacheKeys();

    
}
