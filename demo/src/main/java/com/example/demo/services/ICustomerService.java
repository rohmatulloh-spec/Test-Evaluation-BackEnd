package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.models.CustomerModel;

@Service
public interface ICustomerService {
    
    Page<CustomerModel> findAllCustomers(Pageable pageable);
    
    Page<CustomerModel> findByCustomerName(String customerName, Pageable pageable);

    List<CustomerResponse> convertToCustomerResponses(Page<CustomerModel> customerModels);

}