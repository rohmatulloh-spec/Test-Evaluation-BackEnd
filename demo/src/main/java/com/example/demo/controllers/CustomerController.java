package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.web.bind.annotation.GetMapping;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.dto.pagination.CustomerPagination;
import com.example.demo.dto.pagination.ItemsInfo;
import com.example.demo.dto.response.CustomerListResponse;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.models.CustomerModel;
import com.example.demo.services.ICustomerService;
import com.example.demo.services.cache.ICacheService;
import com.example.demo.services.specifications.CustomerSortOrder;
import com.google.gson.Gson;


@RequestMapping("/api")
@RestController
@EnableScheduling
public class CustomerController {
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ICustomerService iCustomerService;

    @Autowired
    private ICacheService iCacheService;

    private final AtomicLong lastAccessTime = new AtomicLong(System.currentTimeMillis());

    @Bean
    public AtomicLong lastAccessTime() {
        return lastAccessTime;
    }

     @GetMapping("/customers")
     public ResponseEntity<?> getAllCustomers(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) CustomerSortOrder sortOrder,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        // Update last access time
        lastAccessTime.set(System.currentTimeMillis());

        // Check Redis cache first
        String cacheKey = iCacheService.generateCustomerCacheKey(pageNo, pageSize);
        String cachedResult = redisTemplate.opsForValue().get(cacheKey);

        if (cachedResult != null) {
            // Return cached result
            System.out.println("Data retrieved from Redis cache");
            CustomerListResponse cachedResponse = new Gson().fromJson(cachedResult, CustomerListResponse.class);
            return new ResponseEntity<>(cachedResponse, HttpStatus.OK);
        }

        System.out.println("Data retrieved from Mysql");

        Sort sort = (sortOrder != null) ?
                Sort.by(Sort.Order.by(sortOrder.getFieldName()).with(sortOrder.getDirection())) :
                Sort.by(Sort.Order.by("customerName").with(Sort.Direction.ASC));

        Pageable paging = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<CustomerModel> pageResult;

        if (customerName != null && !customerName.isEmpty()) {
            pageResult = iCustomerService.findByCustomerName(customerName, paging);
            if (pageResult.isEmpty()) {
                return new ResponseEntity<>("Customer name '" + customerName + "' not found", HttpStatus.NOT_FOUND);
            }
        } else {
            pageResult = iCustomerService.findAllCustomers(paging);
        }

        List<CustomerResponse> customerResponses = iCustomerService.convertToCustomerResponses(pageResult);

        ItemsInfo itemsInfo = new ItemsInfo(
                customerResponses != null ? customerResponses.size() : 0,
                (int) pageResult.getTotalElements(),
                pageSize);

        int lastVisiblePage = pageResult.getTotalPages() > 0 ? pageResult.getTotalPages() : 1;

        CustomerPagination paginationInfo = new CustomerPagination(
                pageNo,
                pageResult.hasNext(),
                itemsInfo,
                lastVisiblePage);

        CustomerListResponse response = new CustomerListResponse(customerResponses, paginationInfo);

        if (customerResponses == null || customerResponses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Store in Redis cache
        String jsonResponse = new Gson().toJson(response);
        redisTemplate.opsForValue().set(cacheKey, jsonResponse);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}