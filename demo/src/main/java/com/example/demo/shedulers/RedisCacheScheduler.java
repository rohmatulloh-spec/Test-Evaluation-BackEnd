package com.example.demo.shedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class RedisCacheScheduler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private AtomicLong lastAccessTime = new AtomicLong(0);
    private boolean initialized = false;

    @Scheduled(fixedRate = 60000)  // Run every 1 minute
    public void clearRedisCache() {
        if (!initialized) {
            System.out.println("Scheduler initialized. Waiting before first cache check.");
            initialized = true;
            return;
        }

        long currentTime = System.currentTimeMillis();

        // If more than 1 minute since last access, perform cache clearing
        if ( currentTime - lastAccessTime.get() > 60000) {
            System.out.println("Checking and flushing Redis cache data");

            // Clearing customers data from Redis
            Long deletedCustomers = redisTemplate.delete(redisTemplate.keys("customers:*"));
            System.out.println("Removed " + deletedCustomers + " customers cache keys from Redis");

           
            lastAccessTime.set(currentTime);
        } else {
            System.out.println("No cache clearing needed at this time.");
        }
    }
}
