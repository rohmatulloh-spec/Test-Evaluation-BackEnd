package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.models.CustomerModel;
import com.example.demo.repositories.CustomerRepository;

@Service
public class CustomerServiceImplement implements ICustomerService{
 
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Page<CustomerModel> findAllCustomers(Pageable pageable) {
        return customerRepository.findAllByIsActiveTrue(pageable);
    }

    @Override
    public Page<CustomerModel> findByCustomerName(String customerName, Pageable pageable) {
        return customerRepository.findByCustomerNameIgnoreCase(customerName, pageable);
    }

    @Override
    public List<CustomerResponse> convertToCustomerResponses(Page<CustomerModel> customerModels) {
    return customerModels.stream()
                .filter(CustomerModel::getIsActive)
                .map(this::mapToCustomerDetailResponse)
                .collect(Collectors.toList());
    }

    private CustomerResponse mapToCustomerDetailResponse(CustomerModel customerModel) {
        return new CustomerResponse(
                customerModel.getCustomerId(),
                customerModel.getCustomerName(),
                customerModel.getCustomerEmail(),
                customerModel.getIsActive());

    }

}
